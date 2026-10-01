# Teleport Crystals

Ported from Minecraft 1.21.1 (Yarn mappings) to **Minecraft 26.3** (Fabric,
Mojang's official mappings - Yarn is no longer used as of 26.1).

- **Teleport Stone** - amethyst shards, an iron ingot, ender pearls.
  Sneak + use to bind your current position, use again to teleport there.
  Consumed on use.
- **Teleport Wand** - amethyst shards, a netherite ingot, blaze rods,
  ender pearls. Same behavior, but has durability instead, and can take
  Unbreaking / Mending.

Each item shows one of 4 baked textures depending on what's bound: clear
when unbound, green (Overworld), red (Nether), purple (End).

## Building

Push this to GitHub (see the earlier walkthrough) - `.github/workflows/build.yml`
now uses Java 25 and Gradle 8.12. Or locally: install Java 25 and Gradle
8.12+, then run `gradle build` from this folder (no wrapper is bundled).

**Before building**, double check `gradle.properties` and `build.gradle`
against <https://fabricmc.net/develop/> for 26.3 - the exact Loom plugin
version, Fabric Loader version, and Fabric API version are all moving
targets right now and the ones here are a best-effort snapshot.

## What changed from the 1.21.1 version

26.1 was a landmark Minecraft release: the game became fully unobfuscated
and Fabric dropped Yarn mappings for Mojang's official ones, Java jumped
to 25, and there were sweeping internal changes (rendering, recipes,
item/component internals, NBT API). This is a from-scratch rewrite against
official mappings, not a patch. Notable changes:

- Every vanilla class name changed: `World`->`Level`, `PlayerEntity`->`Player`,
  `ServerPlayerEntity`->`ServerPlayer`, `ItemStack` (same), `Item.Settings`->`Item.Properties`,
  `.maxCount()`->`.stacksTo()`, `.maxDamage()`->`.durability()`,
  `NbtCompound`->`CompoundTag`, `RegistryKey`->`ResourceKey`,
  `Text`->`Component`, `Hand`->`InteractionHand`, `TypedActionResult`->`InteractionResultHolder`,
  `SoundCategory`->`SoundSource`, `DataComponentTypes`->`DataComponents`,
  and (surprisingly) Yarn's `Identifier` name won out over the old
  `ResourceLocation` when Mojang renamed it at 1.21.11.
- `stack.damage(...)` -> `stack.hurtAndBreak(int, ServerLevel, ServerPlayer, Consumer<Item>)`.
- Since 1.21.5, `CompoundTag` getters return `Optional<T>` unless you pass
  a fallback value (`nbt.getInt("key", 0)`), in which case you get the
  plain type back - used throughout `TeleportData`.
- `CustomModelDataComponent` (a single float) became `CustomModelData`
  (four separate lists: floats/flags/strings/colors). The item texture
  switching now uses the **strings** list plus the newer
  `minecraft:select` item-model system (`assets/.../items/*.json`),
  replacing the old float-threshold `overrides` list entirely.
- Recipe JSON's `result` field is `"id"` now, not `"item"` (ingredients in
  `key` still use `"item"`).
- Fabric API renamed `ItemGroupEvents` to `CreativeModeTabEvents`.
- Dropped the custom tooltip and the enchant-table-strength tweak from
  the 1.21.1 version - both are deprecated/reworked APIs right now and
  weren't essential to the item working. Happy to add them back once the
  core mod is confirmed running.

## Update: checked against the real 26.3 docs

I went back through the 5 uncertain spots below with an actual web search
against Fabric's live docs/blog/maven javadocs (this project's Minecraft
26.3 released Sept 15, 2026, after my training cutoff, so I can't just
recall this - I looked it up):

1. **`ServerPlayer#teleportTo(...)`** - confirmed correct. The vanilla
   signature is `teleportTo(ServerLevel, double, double, double,
   Set<RelativeMovement>, float, float)` returning `boolean`, exactly
   what `TeleportCrystalItem` calls.
2. `SoundEvents.AMETHYST_BLOCK_CHIME` / `ENDERMAN_TELEPORT` - left as-is;
   still unverified, but cosmetic only if wrong.
3. **Fixed a real bug**: `CreativeModeTabEvents` is not under
   `net.fabricmc.fabric.api.itemgroup.v1` - it's under
   `net.fabricmc.fabric.api.creativetab.v1`. `ModItems.java`'s import
   is now corrected. This one would have failed to compile.
4. `enchantable/durability` item tag - left as-is; this predates 26.x
   and nothing in the 26.3 changelog touches it.
5. **Updated build versions** for 26.3, per fabricmc.net's Sept 2026
   post: Fabric Loader `0.19.5` (was 0.19.0), Loom `1.17-SNAPSHOT` (was
   1.11-SNAPSHOT), Gradle `9.6.0` in the CI workflow (was 8.12).
   `fabric_version` (`0.161.0+26.3`) was already correct.

None of 26.3's actual changes (fuel/compost components, brewing recipes,
block transformers, world-gen registries) touch anything this mod uses,
so the core logic shouldn't need further changes for this specific
Minecraft version - just build-tool versions and that one import.

## Round 2: fixes from an actual `:compileJava` failure

The first build attempt did fail, on API changes that are real but
happened earlier than 26.1-26.3 (further back in the 1.21.x line), so
they weren't things the 26.3 changelog would mention. Verified each one
against Fabric's own docs / Mojang's mapping history before applying:

- **`Item#use` no longer returns `InteractionResultHolder<ItemStack>`**
  - since 1.21.3 it returns `InteractionResult` directly (the stack is
  mutated in place instead). `TeleportCrystalItem.use` now returns
  `InteractionResult` and every `InteractionResultHolder.success(stack)` /
  `.fail(stack)` became plain `InteractionResult.SUCCESS` / `.FAIL`.
- **`RelativeMovement` was renamed `Relative`** (Mojang mapping rename,
  around 1.21.3-1.21.4) - `net.minecraft.world.entity.Relative` now,
  same `Set<Relative>` usage in `teleportTo(...)`.
- **`ResourceKey#location()` was renamed `identifier()`** (at 1.21.11,
  alongside the `ResourceLocation`->`Identifier` class rename) - fixed
  in both `TeleportData` and `DimensionColor`.
- **`CompoundTag`'s fallback getters aren't overloads of the same name.**
  `getInt(key)` / `getString(key)` return `Optional<T>`; the
  fallback-taking version is a differently named method -
  `getIntOr(key, fallback)`, `getStringOr(key, fallback)` - not a second
  `getInt(key, fallback)` overload as I'd assumed. Fixed in `TeleportData`.
- **`ServerPlayer#getServer()` is gone**; get the `MinecraftServer` off
  the level instead (`level.getServer()`, on the now-confirmed
  `ServerLevel` after a level instanceof check) rather than off the
  player.

## Round 3: another real compile error

- **`teleportTo(...)` takes a trailing `boolean` now** (`dismountVehicle`).
  Added `true` as the 8th argument, so the player dismounts any vehicle
  before teleporting - reasonable default for a teleport crystal. Pass
  `false` instead if you'd rather they keep riding through.

Unlike the round-2 fixes, I couldn't independently cross-check this one
against another source the way I did the others - I applied it because
it matches the actual compiler error you got, but keep an eye on it.

Paste any further build error back and I'll fix the exact line.

## Round 4: this one was a runtime crash, not a compile error

The jar built and loaded far enough to reach `ModItems`, which is
progress - Fabric API being installed cleared the earlier "incompatible
mods" screen. The crash was:

```
NullPointerException: Item id not set
	at Item$Properties.itemIdOrThrow
	at Item$Properties.effectiveDescriptionId
	at Item.<init>
```

**Cause:** an `Item`'s registry id now has to be set on its `Properties`
*before* the item is constructed - the constructor reads it immediately.
The old code built the `TeleportStoneItem`/`TeleportWandItem` instances
first and only figured out their id afterward, in `register(...)`, which
is too late.

**Fix**, confirmed against Fabric's own current docs (which show this
exact pattern): `ModItems.register` now takes a factory function instead
of an already-built item. It creates the `ResourceKey<Item>` first, calls
`.setId(key)` on a fresh `Item.Properties`, *then* hands that to the
factory to actually construct the item, then registers it under the same
key.

Paste any further build/crash log back and I'll fix the exact line.

## Round 5: Loom "No matching variant" build failure

An outside diagnosis said to drop Java 25 to Java 21. That is wrong:
Fabric's own porting docs say to set Java compatibility to 25 for 26.x,
and the game itself runs on Java 25. Keep Java 25.

The real cause was a change to the build setup: Loom was set to `'1.+'`
plus a `useModule(...)` hack in `settings.gradle`. That makes Gradle
resolve Loom as an ordinary library instead of a plugin, and then no
variant matches. Reverted to the plain setup Fabric's example mod uses:
`id 'net.fabricmc.fabric-loom' version '1.17-SNAPSHOT'`, and
`settings.gradle` back to just the Fabric maven repo. That combination
got past plugin resolution and into `compileJava` earlier.

Also merged in: the recipe files now use plain-string ingredients
(`"A": "minecraft:amethyst_shard"`), the format 1.21.2+ expects.
Loader version is back to 0.19.5.

## Round 6: it's alive - durability + rename

It built and runs. Two tweaks:

- **Wand durability 96 -> 24.** `hurtAndBreak(1, ...)` costs 1 point per
  teleport, so 24 durability = 24 uses before it breaks.
- **Display name "Teleport Wand" -> "Teleport Crystal"**, in
  `en_us.json` only. The internal id (`teleportcrystals:teleport_wand`)
  is unchanged - same item id, recipe file, model/texture file names -
  so nothing else needed to move. If you'd rather the id itself say
  `teleport_crystal` (matters if you want the item's file names /
  `/give` command to match too), say so and I'll rename the id and every
  file/reference that points at it, not just the label.

## Round 7: full rename, teleport_wand -> teleport_crystal

Went ahead with the full rename everywhere:

- Item id: `teleportcrystals:teleport_wand` -> `teleportcrystals:teleport_crystal`
  (`ModItems.java`, both the field `TELEPORT_WAND` -> `TELEPORT_CRYSTAL`
  and the registered path).
- Item model selector: `items/teleport_wand.json` -> `items/teleport_crystal.json`.
- All 4 models and all 4 textures: `teleport_wand_{clear,end,nether,overworld}`
  -> `teleport_crystal_{clear,end,nether,overworld}` (models' internal
  texture references updated too).
- Recipe file: `recipe/teleport_wand.json` -> `recipe/teleport_crystal.json`,
  result id updated.
- `enchantable/durability` tag entry updated to the new id.
- Lang key: `item.teleportcrystals.teleport_wand` -> `..._crystal` (already
  renamed last round; unchanged here).

**One thing I deliberately left alone:** the Java class is still named
`TeleportWandItem`. I can't rename it to `TeleportCrystalItem` - that
name's already taken by the shared abstract base class both crystals
extend. It's a compile-time-only name (players never see it, and nothing
in-game references it), so it doesn't affect consistency of anything the
player interacts with - `/give`, tooltips, JSON files, and the item id
are all `teleport_crystal` now. Say the word if you'd rather I rename the
class anyway (e.g. to `TeleportCrystalWandItem`) purely for source
tidiness.

Since the item id changed, `/give @s teleportcrystals:teleport_wand` no
longer works - use `teleportcrystals:teleport_crystal` instead. Any
existing saved item stacks or backed-up crafting recipes referencing the
old id are why you're keeping last round's build as a backup.

## Round 8: crash was the wrong jar, not a bug

`ClassNotFoundException: ...TeleportCrystalsMod` plus a mod version of
literally `${version}` (unsubstituted) in the log meant the file in the
mods folder was `teleportcrystals-1.0.0-sources.jar`, not
`teleportcrystals-1.0.0.jar`. The sources jar (from `withSourcesJar()`
in `build.gradle`) only contains raw `.java` text and unprocessed
resources - no compiled `.class` files at all, so there's genuinely
nothing for Fabric to load.

The GitHub Actions artifact zip bundles both jars together, so it's an
easy mix-up. Updated `build.yml` to only upload the real, compiled jar
going forward (excludes `*-sources.jar`) so this can't happen again from
a future Actions build. Make sure the *current* mods folder has
`teleportcrystals-1.0.0.jar`, not the `-sources` one.

## Round 9: Spacial Ore + Spacial Shard

New content:

- **`teleportcrystals:spacial_ore`** block - generates in the End, only
  in the three "outer island" biomes (`end_highlands`, `end_midlands`,
  `end_barrens`) - deliberately excluding the central main island
  (`the_end`) and `small_end_islands` (the scattered specks out in the
  void), per your "no random bits out in the void" ask. Wired up via
  Fabric's `BiomeModifications` API (`fabric-biome-api-v1`, already
  pulled in by `fabric-api`) rather than overriding the vanilla biome
  JSON files directly.
- **`teleportcrystals:spacial_shard`** item - drops from mining the ore.
  Now replaces the amethyst shard in the Netherite `teleport_crystal`
  recipe (the cheaper `teleport_stone` recipe still uses amethyst shard -
  only the Netherite one changed, as asked).
- Requires a diamond pickaxe or better (`#minecraft:needs_diamond_tool`,
  same tag ancient debris and obsidian use) and only drops shards with
  the right tool.
- Silk Touch drops the ore block itself; otherwise it drops shards, with
  Fortune scaling identically to diamond ore (`minecraft:apply_bonus` /
  `minecraft:ore_drops` formula - loot table verified against the exact
  format vanilla ore loot tables use in this version line).

**Two things I couldn't verify by testing, since I can't run the game:**

1. **Vein size.** Data-driven ore features take one `size` number (set to
   4) that approximates a blob shape - there's no min/max range knob for
   "3 to 5 ore" specifically. Real generated veins will vary somewhat
   around that.
2. **Rarity and height range.** Set to a `rarity_filter` chance of 10
   (roughly 1-in-10 chunks in those biomes even attempts a spawn) across
   Y -20 to 100, as a first guess at "between diamond and ancient
   debris." Both are single numbers in
   `data/teleportcrystals/worldgen/placed_feature/spacial_ore.json` you
   can tune directly after playtesting - lower the `chance` number for
   more common, raise it for rarer.

**About the ore texture:** `spacialore.jpg` was a JPEG - Minecraft only
reads PNG for textures, so I converted it (no visual change, just
re-encoded) to `textures/block/spacial_ore.png`. You don't need to
change anything in your own files; just know that if you replace this
texture yourself later, save it as a `.png`, not `.jpg`.

## Round 10: SoundType compile error

`import net.minecraft.sounds.SoundType;` was wrong - checked against
Mojang's own mapping history, `SoundType` lives at
`net.minecraft.world.level.block.SoundType` (it's a block-state concept,
not a sound-registry one - `net.minecraft.sounds` is for `SoundEvents`
like the ones `TeleportCrystalItem` plays, a different class entirely).
`AMETHYST_CLUSTER` is confirmed as a real constant there, so no other
change was needed once the import was fixed.

## Round 11: soundType() -> sound()

One more naming issue in the same line - checked against Mojang's
mapping history across every version listed (1.16.5 through the current
one): the method has always been called `sound(SoundType)`, never
`soundType(...)`. `soundType` is the *field* name, not the setter -
easy mix-up, now fixed.

## Round 12: two real 26.3 data-pack format changes

Found Minecraft's own official 26.3 patch notes (published the day this
version shipped) and they document exactly what changed - not guesswork:

1. **`worldgen/configured_feature` was merged into `worldgen/feature`,
   and the nested `"config"` object is gone - its fields (`size`,
   `targets`, etc.) now sit directly at the top level next to `"type"`.**
   Moved `data/teleportcrystals/worldgen/configured_feature/spacial_ore.json`
   to `data/teleportcrystals/worldgen/feature/spacial_ore.json` and
   flattened it. This was the actual cause of the "Unbound values ...
   worldgen/feature: [teleportcrystals:spacial_ore]" error - the game
   was looking in `worldgen/feature` for our feature and it wasn't there.
   (`worldgen/placed_feature` is unaffected and still references
   `"feature": "teleportcrystals:spacial_ore"` the same way.)
2. **Block State fields were renamed: `Name` -> `id`, `Properties` ->
   `properties`** - so the ore target's `"state": {"Name": ...}` had to
   become `"state": {"id": ...}`. This is inside the same file as fix 1.
3. **Loot table field renames**, which would have crashed the game the
   moment the ore was actually mined, even though it hadn't triggered
   yet: a loot entry's `conditions` (list) is now `condition` (a single
   value), `functions` is now `modifier`, and inside each condition/
   function object the type-discriminator key changed from `condition`/
   `function` to just `type`. Rewrote `spacial_ore`'s loot table to
   match. I have not been able to see this one actually succeed in-game
   (nothing mines the ore in a log file), so if breaking the ore still
   errors, send that log.

Note: an earlier round in this README cited a third-party wiki entry
labeled "Validated for Minecraft Java 26.2" for the loot table's
`conditions`/`functions` format - that source was correct for 26.2, but
26.3 changed it out from under it. Version-pinned references like that
are only as good as the version they were checked against.

## Round 13: missing required field in the new flattened format

Progress: the file-move fix worked - the game found
`teleportcrystals:spacial_ore` this time instead of calling it unbound.
The new error was a parse failure: `No key discard_chance_on_air_exposure`.

In 26.3's flattened ore feature format, `discard_chance_on_air_exposure`
is apparently a required field with no default (previously, nested under
`config`, it could be omitted). Added it set to `0.0` (never discard, so
behavior matches what we had before this field existed). If you'd rather
ore touching open air/void near an island's edge just not generate there,
raise this toward `1.0` - that's a free knob to tune the "no random bits
out in the void" feel further, on top of the biome restriction already
doing the heavy lifting there.
