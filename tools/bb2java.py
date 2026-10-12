import json, math, sys
m = json.load(open('/mnt/user-data/uploads/spacial_lizard_1_.bbmodel'))
els = {e['uuid']: e for e in m['elements']}
groups = {g['uuid']: g for g in m['groups']}
SKIP_GROUPS = {'hitbox'}
FACING_YAW = -math.pi/2   # bb model faces -X; Mojang models face -Z
warnings = []
TARGET_LENGTH = {'dive': 3.0}   # seconds; the dive is stretched to this length

def f(x):
    x = round(float(x), 5)
    s = repr(x)
    if s.endswith('.0'): s = s[:-2]
    return s + 'F'

def rad(d): return math.radians(d)

def tex_off(e):
    fr, to = e['from'], e['to']
    dz = abs(to[2]-fr[2])
    up = e['faces']['up']['uv']
    return max(0, int(round(up[0]-dz))), max(0, int(round(up[1])))

lines = []
def emit_group(g, parent_pivot, parent_var, depth):
    name = g['name']
    if name in SKIP_GROUPS: return
    px, py, pz = g['origin']
    rx, ry, rz = g.get('rotation', [0,0,0])
    var = 'p%d_%s' % (len(lines), ''.join(c if c.isalnum() else '_' for c in name))
    if parent_pivot is None:
        off = (px, -py, pz)          # relative to the container at (0,24,0)
    else:
        off = (px-parent_pivot[0], parent_pivot[1]-py, pz-parent_pivot[2])
    own, rotated = [], []
    for c in g.get('children', []):
        if isinstance(c, str):
            e = els[c]
            if e['name'] in SKIP_GROUPS or not e.get('visibility', True) and e['name']=='hitbox': continue
            if any(abs(v) > 1e-6 for v in e.get('rotation', [0,0,0])): rotated.append(e)
            else: own.append(e)
    cl = 'CubeListBuilder.create()'
    for e in own:
        fr, to = e['from'], e['to']
        u, v = tex_off(e)
        cl += '\n\t\t\t.texOffs(%d, %d).addBox(%s, %s, %s, %s, %s, %s)' % (
            u, v, f(fr[0]-px), f(py-to[1]), f(fr[2]-pz), f(to[0]-fr[0]), f(to[1]-fr[1]), f(to[2]-fr[2]))
    pose = 'PartPose.offsetAndRotation(%s, %s, %s, %s, %s, %s)' % (
        f(off[0]), f(off[1]), f(off[2]), f(rad(-rx)), f(rad(ry)), f(rad(-rz)))
    lines.append('\t\tPartDefinition %s = %s.addOrReplaceChild("%s", %s,\n\t\t\t%s);' % (var, parent_var, name, cl, pose))
    for i, e in enumerate(rotated):
        fr, to = e['from'], e['to']
        ox, oy, oz = e.get('origin', [px, py, pz])
        rx2, ry2, rz2 = e['rotation']
        u, v = tex_off(e)
        cname = '%s_cube%d' % (name, i)
        cvar = var + '_c%d' % i
        lines.append('\t\tPartDefinition %s = %s.addOrReplaceChild("%s",\n\t\t\tCubeListBuilder.create().texOffs(%d, %d).addBox(%s, %s, %s, %s, %s, %s),\n\t\t\tPartPose.offsetAndRotation(%s, %s, %s, %s, %s, %s));' % (
            cvar, var, cname, u, v,
            f(fr[0]-ox), f(oy-to[1]), f(fr[2]-oz), f(to[0]-fr[0]), f(to[1]-fr[1]), f(to[2]-fr[2]),
            f(ox-px), f(py-oy), f(oz-pz), f(rad(-rx2)), f(rad(ry2)), f(rad(-rz2))))
    for c in g.get('children', []):
        if not isinstance(c, str):
            emit_group(groups[c['uuid']], (px, py, pz), var, depth+1)

# outliner: top-level entries are dicts with uuid referencing groups, or cube uuid strings
def build_outliner(nodes):
    for n in nodes:
        if isinstance(n, dict):
            g = dict(groups[n['uuid']]); g['children'] = n.get('children', [])
            groups[n['uuid']] = g
            build_outliner([c for c in n.get('children', []) if isinstance(c, dict)])
build_outliner(m['outliner'])
for n in m['outliner']:
    if isinstance(n, dict): emit_group(groups[n['uuid']], None, 'container', 0)

model_body = '\n'.join(lines)

java_model = '''package net.example.teleportcrystals.client.model;

// GENERATED from spacial_lizard.bbmodel by gen/bb2java.py - do not hand-edit.
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.animation.KeyframeAnimation;
import net.example.teleportcrystals.client.animation.SpacialLizardAnimations;
import net.example.teleportcrystals.client.state.SpacialLizardRenderState;

public class SpacialLizardModel extends EntityModel<SpacialLizardRenderState> {
	private final KeyframeAnimation lick;
	private final KeyframeAnimation walk;
	private final KeyframeAnimation dive;
	private final KeyframeAnimation surface;

	public SpacialLizardModel(ModelPart root) {
		super(root);
		this.lick = SpacialLizardAnimations.LICK.bake(root);
		this.walk = SpacialLizardAnimations.WALK.bake(root);
		this.dive = SpacialLizardAnimations.DIVE.bake(root);
		this.surface = SpacialLizardAnimations.SURFACE.bake(root);
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition rootDef = mesh.getRoot();
		// The Blockbench model faces -X; Minecraft models face -Z, so everything hangs off a rotated container.
		// If the lizard walks backwards in-game, change FACING_YAW's sign (or add PI).
		PartDefinition container = rootDef.addOrReplaceChild("container", CubeListBuilder.create(),
			PartPose.offsetAndRotation(0F, 24F, 0F, 0F, %(yaw)s, 0F));
%(body)s
		return LayerDefinition.create(mesh, 64, 64);
	}

	@Override
	public void setupAnim(SpacialLizardRenderState state) {
		super.setupAnim(state);
		if (state.diveAnimationState.isStarted()) {
			this.dive.apply(state.diveAnimationState, state.ageInTicks);
		}
		if (state.surfaceAnimationState.isStarted()) {
			this.surface.apply(state.surfaceAnimationState, state.ageInTicks);
		}
		if (state.walkAnimationState.isStarted()) {
			this.walk.apply(state.walkAnimationState, state.ageInTicks);
		}
		if (state.lickAnimationState.isStarted()) {
			this.lick.apply(state.lickAnimationState, state.ageInTicks);
		}
	}
}
''' % {'yaw': f(FACING_YAW), 'body': model_body}

# ---- animations ----
def num(v, where):
    try: return float(v)
    except Exception:
        warnings.append('non-numeric keyframe value %r in %s -> 0' % (v, where)); return 0.0

anim_names = {'lick': 'LICK', 'walk': 'WALK', 'dive': 'DIVE', 'surface': 'SURFACE'}
adefs = []
for a in m['animations']:
    const = anim_names.get(a['name'].lower())
    if not const:
        warnings.append('animation %r has no mapping, skipped' % a['name']); continue
    stretch = TARGET_LENGTH.get(a['name'].lower(), a['length']) / a['length']
    b = 'AnimationDefinition.Builder.withLength(%s)' % f(a['length'] * stretch)
    if a.get('loop') == 'loop': b += '\n\t\t\t.looping()'
    for uid, an in a['animators'].items():
        gname = groups[uid]['name'] if uid in groups else an.get('name')
        if gname in SKIP_GROUPS or uid not in groups: continue
        chans = {}
        for k in an.get('keyframes', []):
            chans.setdefault(k['channel'], []).append(k)
        for ch, ks in chans.items():
            ks.sort(key=lambda k: k['time'])
            tgt = {'rotation': 'ROTATION', 'position': 'POSITION', 'scale': 'SCALE'}.get(ch)
            if not tgt: continue
            kfs = []
            for k in ks:
                dp = k['data_points'][0]
                x, y, z = (num(dp.get(c, 0), a['name']+'/'+gname) for c in 'xyz')
                if ch == 'rotation': vec = 'KeyframeAnimations.degreeVec(%s, %s, %s)' % (f(-x), f(y), f(-z))
                elif ch == 'position': vec = 'KeyframeAnimations.posVec(%s, %s, %s)' % (f(x), f(-y), f(z))
                else: vec = 'KeyframeAnimations.scaleVec(%s, %s, %s)' % (f(x), f(y), f(z))
                interp = 'CATMULLROM' if k.get('interpolation') == 'catmullrom' else 'LINEAR'
                if k.get('interpolation') not in ('linear', 'catmullrom', None):
                    warnings.append('interpolation %r on %s/%s -> LINEAR' % (k.get('interpolation'), a['name'], gname))
                kfs.append('new Keyframe(%s, %s, AnimationChannel.Interpolations.%s)' % (f(k['time'] * stretch), vec, interp))
            b += '\n\t\t\t.addAnimation("%s", new AnimationChannel(AnimationChannel.Targets.%s,\n\t\t\t\t%s))' % (gname, tgt, ',\n\t\t\t\t'.join(kfs))
    b += '\n\t\t\t.build()'
    adefs.append('\tpublic static final AnimationDefinition %s = %s;' % (const, b))

java_anim = '''package net.example.teleportcrystals.client.animation;

// GENERATED from spacial_lizard.bbmodel by gen/bb2java.py - do not hand-edit.
import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

public class SpacialLizardAnimations {
%s
}
''' % '\n\n'.join(adefs)

import os
base = '/home/claude/mod/extracted/teleportcrystals/src/main/java/net/example/teleportcrystals/client'
os.makedirs(base + '/model', exist_ok=True); os.makedirs(base + '/animation', exist_ok=True)
open(base + '/model/SpacialLizardModel.java', 'w').write(java_model)
open(base + '/animation/SpacialLizardAnimations.java', 'w').write(java_anim)
print('parts emitted:', len(lines), '| animations:', [k for k in anim_names.values() if k in java_anim])
print('WARNINGS:'); [print(' -', w) for w in warnings]
