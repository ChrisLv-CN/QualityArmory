package me.zombie_striker.customitemmanager.versions.V1_14;

import me.zombie_striker.customitemmanager.AbstractItem;
import me.zombie_striker.customitemmanager.AbstractItemFact;
import me.zombie_striker.customitemmanager.CustomItemManager;
import me.zombie_striker.customitemmanager.MaterialStorage;
import me.zombie_striker.qg.api.QualityArmory;
import me.zombie_striker.qg.config.GunYMLCreator;
import me.zombie_striker.qg.guns.projectiles.ProjectileManager;
import me.zombie_striker.qg.guns.utils.WeaponSounds;
import me.zombie_striker.qg.guns.utils.WeaponType;
import me.zombie_striker.qg.handlers.IronsightsHandler;
import me.zombie_striker.qg.handlers.MultiVersionLookup;
import me.zombie_striker.qg.handlers.chargers.ChargingManager;
import me.zombie_striker.qg.handlers.reloaders.ReloadingManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CrossbowMeta;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CustomGunItem extends AbstractItem {

	@Override
	public ItemStack getItem(Material material, int data, int variant) {
		ItemStack is = QualityArmory.getCustomItem(QualityArmory.getCustomItem(material,data,variant));
		if(is.getType()==Material.CROSSBOW){
			//Now the player will hold the crossbow like a gun
			CrossbowMeta im = (CrossbowMeta) is.getItemMeta();
			im.addChargedProjectile(new ItemStack(Material.VOID_AIR));
			is.setItemMeta(im);
		}
		return is;
	}


	@Override
	public boolean isCustomItem(ItemStack is) {
		return QualityArmory.isCustomItem(is);
	}

	@Override
	public void initItems(File dataFolder) {
		CustomItemManager.setResourcepack("https://www.dropbox.com/s/wh495z84cndyeji/QualityArmoryV2.0.0.zip?dl=1");





		List<String> stringsWoodRif = Arrays.asList(new String[]{getIngString(Material.IRON_INGOT, 0, 12),
				getIngString(MultiVersionLookup.getWood(), 0, 2), getIngString(Material.REDSTONE, 0, 5)});
		List<String> stringsGoldRif = Arrays.asList(new String[]{getIngString(Material.IRON_INGOT, 0, 12),
				getIngString(Material.GOLD_INGOT, 0, 2), getIngString(Material.REDSTONE, 0, 5)});
		List<String> stringsMetalRif = Arrays.asList(
				new String[]{getIngString(Material.IRON_INGOT, 0, 15), getIngString(Material.REDSTONE, 0, 5)});
		List<String> stringsPistol = Arrays.asList(
				new String[]{getIngString(Material.IRON_INGOT, 0, 5), getIngString(Material.REDSTONE, 0, 2)});
		List<String> stringsRPG = Arrays.asList(
				new String[]{getIngString(Material.IRON_INGOT, 0, 32), getIngString(Material.REDSTONE, 0, 10)});

		List<String> stringsHelmet = Arrays.asList(
				new String[]{getIngString(Material.IRON_INGOT, 0, 5), getIngString(Material.OBSIDIAN, 0, 1)});

		List<String> stringsGrenades = Arrays.asList(new String[]{getIngString(Material.IRON_INGOT, 0, 6),
				getIngString(MultiVersionLookup.getGunpowder(), 0, 10)});

		List<String> stringsAmmo = Arrays.asList(new String[]{getIngString(Material.IRON_INGOT, 0, 1),
				getIngString(MultiVersionLookup.getGunpowder(), 0, 1), getIngString(Material.REDSTONE, 0, 1)});
		List<String> stringsAmmoMusket = Arrays.asList(new String[]{getIngString(Material.IRON_INGOT, 0, 4),
				getIngString(MultiVersionLookup.getGunpowder(), 0, 3),});
		List<String> stringsAmmoRPG = Arrays.asList(new String[]{getIngString(Material.IRON_INGOT, 0, 4),
				getIngString(MultiVersionLookup.getGunpowder(), 0, 6), getIngString(Material.REDSTONE, 0, 1)});

		List<String> StringsWool = Arrays.asList(new String[]{getIngString(MultiVersionLookup.getWool(), 0, 8)});

		List<String> stringsHealer = Arrays.asList(new String[]{getIngString(MultiVersionLookup.getWool(), 0, 6),
				getIngString(Material.GOLDEN_APPLE, 0, 1)});
		List<String> stringsMini = Arrays.asList(
				new String[] { getIngString(Material.IRON_INGOT, 0, 10), getIngString(Material.TNT, 0, 16) });

		List<String> strings10mm = Arrays.asList(new String[] { getIngString(Material.IRON_INGOT, 0, 10),
				getIngString(Material.REDSTONE, 0, 4) });





		GunYMLCreator.createAmmo(false, dataFolder, false, "9mm", "&f9mm", 1, stringsAmmo, 2, 0.7, 50,
				10).setMaterial(Material.PHANTOM_MEMBRANE).done();
		GunYMLCreator.createAmmo(false, dataFolder, false, "556", "&f5.56 NATO", 2, stringsAmmo, 4, 1, 50,
				5).setMaterial(Material.PHANTOM_MEMBRANE).done();
		GunYMLCreator.createAmmo(false,dataFolder, false, "762", "&f7.62x39mm", 3, stringsAmmo, 5, 1.2,
				50, 5).setMaterial(Material.PHANTOM_MEMBRANE).done();
		GunYMLCreator.createAmmo(false, dataFolder, false, "shell", "&fBuckshot", 4, stringsAmmo, 10, 0.5,
				8, 4).setMaterial(Material.PHANTOM_MEMBRANE).done();
		GunYMLCreator.createAmmo(false, dataFolder, false, "rocket", "&fRocket", 5, stringsAmmoRPG, 100,
				1000, 1).setMaterial(Material.PHANTOM_MEMBRANE).done();
		GunYMLCreator.createAmmo(false, dataFolder, false, "musketball", "&fMusket Ball", 6,
				stringsAmmoMusket, 1, 0.7, 32, 8).setMaterial(Material.PHANTOM_MEMBRANE).done();
		GunYMLCreator.createAmmo(false, dataFolder, false, "50bmg", "&f.50BMG", 7, stringsAmmo, 10, 3, 30,
				1).setMaterial(Material.PHANTOM_MEMBRANE).done();
		GunYMLCreator.createAmmo(false, dataFolder, false, "40mm", "&f40x46mm", 8, stringsAmmo, 30, 10,
				10, 1).setMaterial(Material.PHANTOM_MEMBRANE).done();
		GunYMLCreator
				.createAmmo(false, dataFolder, false, "default_flamerfuel", "fuel", "&fFlamerFuel", null,
						Material.BLAZE_POWDER, 0,
						Arrays.asList(new String[] { getIngString(Material.BLAZE_ROD, 0, 1), }), 1, 1, 64, 2)
				.setVariant(1).done();



		GunYMLCreator.createAmmo(true,dataFolder, false, "default_mininuke", "mininuke", "MiniNuke", 9,
				stringsMini, 3000, 100, 1).setMaterial(Material.PHANTOM_MEMBRANE).done();

		GunYMLCreator.createAmmo(true, dataFolder, false, "default_fusion_cell", "fusion_cell",
				"Fusion Cell", 10, strings10mm, 60, 0.2, 30).setMaterial(Material.PHANTOM_MEMBRANE).done();






		GunYMLCreator.createNewDefaultGun(dataFolder, "p30", "P30", 1, stringsPistol, WeaponType.PISTOL,
				null, true, "9mm", 3, 12, 700).setIsSecondaryWeapon(true).setMaterial(Material.CROSSBOW).done();
		GunYMLCreator
				.createNewDefaultGun(dataFolder, "pkp", "PKP", 2, stringsMetalRif, WeaponType.RIFLE,
						WeaponSounds.GUN_BIG, true, "762", 3, 100, 12000)
				.setFullyAutomatic(3).setBulletsPerShot(1).setRecoil(2).setMaterial(Material.CROSSBOW).done();
		GunYMLCreator
				.createNewDefaultGun(dataFolder, "mp5k", "MP5K", 3, stringsMetalRif, WeaponType.SMG,
						WeaponSounds.GUN_SMALL_AUTO, false, "9mm", 2, 32, 2500)
				.setFullyAutomatic(3).setBulletsPerShot(1).setMaterial(Material.CROSSBOW).done();
		GunYMLCreator
				.createNewDefaultGun(dataFolder, "ak47", "AK47", 4, stringsMetalRif, WeaponType.RIFLE,
						null, true, "762", 3, 40, 5000)
				.setSway(0.19).setFullyAutomatic(2).setBulletsPerShot(1).setRecoil(2).setMaterial(Material.CROSSBOW).done();
		GunYMLCreator
				.createNewDefaultGun(dataFolder, "ak47u", "AK47-U", 5, stringsMetalRif, WeaponType.RIFLE,
						null, true, "762", 3, 30, 5000)
				.setFullyAutomatic(2).setBulletsPerShot(1).setRecoil(2).setMaterial(Material.CROSSBOW).done();
		GunYMLCreator.createNewDefaultGun(dataFolder, "m16", "M16", 6, stringsMetalRif, WeaponType.RIFLE,
				null, true, "556", 3, 30, 3600).setFullyAutomatic(2).setBulletsPerShot(1).setMaterial(Material.CROSSBOW).setRecoil(2).done();
		GunYMLCreator
				.createNewDefaultGun(dataFolder, "remington", "Remington", 7, stringsMetalRif,
						WeaponType.SHOTGUN, null, false, "shell", 3, 8, 1000)
				.setChargingHandler(ChargingManager.PUMPACTION)
				.setReloadingHandler(ReloadingManager.PUMPACTIONRELOAD).setBulletsPerShot(20).setDistance(70)
				.setRecoil(10).setMaterial(Material.CROSSBOW).done();
		GunYMLCreator
				.createNewDefaultGun(dataFolder, "fnfal", "FN Fal", 8, stringsMetalRif, WeaponType.RIFLE,
						null, false, "762", 3, 32, 3800)
				.setFullyAutomatic(2).setBulletsPerShot(1).setRecoil(2).setMaterial(Material.CROSSBOW).done();
		GunYMLCreator
				.createNewDefaultGun(dataFolder, "rpg", "RPG", 9, stringsRPG, WeaponType.RPG, null, false,
						"rocket", 100, 1, 4000)
				.setDelayShoot(1).setCustomProjectile(ProjectileManager.RPG)
				.setCustomProjectileExplosionRadius(10).setCustomProjectileVelocity(2)// .setChargingHandler(ChargingManager.RPG)
				.setReloadingHandler(ReloadingManager.SINGLERELOAD).setDistance(500).setMaterial(Material.CROSSBOW).setParticle("SMOKE_LARGE")
				.setRecoil(15).done();
		GunYMLCreator
				.createNewDefaultGun(dataFolder, "ump", "UMP", 10, stringsMetalRif, WeaponType.SMG,
						WeaponSounds.GUN_SMALL_AUTO, false, "9mm", 2, 32, 1700)
				.setFullyAutomatic(2).setBulletsPerShot(1).setMaterial(Material.CROSSBOW).done();
		GunYMLCreator.createNewDefaultGun(dataFolder, "sw1911", "SW-1911", 11, stringsPistol,
				WeaponType.PISTOL, null, true, "9mm", 3, 12, 700).setMaterial(Material.CROSSBOW).setIsSecondaryWeapon(true).done();
		GunYMLCreator
				.createNewDefaultGun(dataFolder, "m40", "M40", 12, stringsWoodRif, WeaponType.SNIPER, null,
						true, "762", 10, 6, 2700)
				.setZoomLevel(9).setDelayShoot(0.7).setChargingHandler(ChargingManager.BOLT).setMaterial(Material.CROSSBOW).setUseOffhand(true)
				.setSwayMultiplier(3).setDistance(280).setRecoil(5).done();
		GunYMLCreator
				.createNewDefaultGun(dataFolder, "enfield", "Enfield", 13, stringsPistol,
						WeaponType.PISTOL, null, true, "9mm", 3, 6, 200)
				.setIsSecondaryWeapon(true).setChargingHandler(ChargingManager.REVOLVER).setMaterial(Material.CROSSBOW)
				.setReloadingHandler(ReloadingManager.SINGLERELOAD).done();
		GunYMLCreator
				.createNewDefaultGun(dataFolder, "henryrifle", "Henry Rifle", 19, stringsGoldRif,
						WeaponType.RIFLE, null, true, "556", 4, 6, 400)
				.setChargingHandler(ChargingManager.BREAKACTION).setMaterial(Material.CROSSBOW)
				.setReloadingHandler(ReloadingManager.SINGLERELOAD).done();
		GunYMLCreator
				.createNewDefaultGun(dataFolder, "mauser", "Mauser C96", 14, stringsPistol,
						WeaponType.PISTOL, null, true, "9mm", 3, 12, 700).setMaterial(Material.CROSSBOW)
				.setSwayMultiplier(3).setIsSecondaryWeapon(true).done();

		GunYMLCreator.createMisc(false, dataFolder, false, "default_grenade", "grenade", "&7Grenade",
				Arrays.asList(ChatColor.DARK_GRAY + "[LMB] to pull pin", ChatColor.DARK_GRAY + "[RMB] to throw",
						ChatColor.DARK_GRAY + "Grenades wait " + ChatColor.GRAY + "FIVE seconds"
								+ ChatColor.DARK_GRAY + " before exploding.",
						ChatColor.DARK_RED + "<!>Will Explode Even If Not Thrown<!>"),
				m(15), stringsGrenades, 100, WeaponType.GRENADES, 100, 1).set(false, "radius", 10).setMaterial(Material.CROSSBOW).done();
		GunYMLCreator
				.createNewDefaultGun(dataFolder, "dragunov", "Dragunov", 16, stringsMetalRif,
						WeaponType.SNIPER, null, true, "762", 7, 12, 2100).setMaterial(Material.CROSSBOW)
			.setUseOffhand(true)	.setDelayShoot(0.4).setZoomLevel(9).setSwayMultiplier(3).setRecoil(5).done();
		GunYMLCreator
				.createNewDefaultGun(dataFolder, "spas12", "Spas-12", 17, stringsMetalRif,
						WeaponType.SHOTGUN, null, false, "shell", 2, 8, 1000).setMaterial(Material.CROSSBOW)
				.setBulletsPerShot(20).setDistance(80).setRecoil(10).done();
		GunYMLCreator
				.createNewDefaultGun(dataFolder, "aa12", "AA-12", 18, stringsMetalRif, WeaponType.SHOTGUN,
						null, false, "shell", 2, 32, 4000).setMaterial(Material.CROSSBOW)
				.setBulletsPerShot(10).setDistance(80).setFullyAutomatic(2).setRecoil(7).done();

		/**
		 * 27 - 36 taken for custom weapons
		 */
	//	GunYMLCreator.createMisc(false,dataFolder, false, "default_Medkit_camo", "medkitcamo", "&5Medkit",
	//			null, m(37), stringsHealer, 300, WeaponType.MEDKIT, 1, 1000).setMaterial(Material.CROSSBOW).done();
//TODO: The medical bag is unneeded. The systems broken, so we can delete it.
		GunYMLCreator
				.createNewDefaultGun(dataFolder, "magnum", "Magnum", 19, stringsPistol, WeaponType.PISTOL,
						WeaponSounds.GUN_BIG, true, "9mm", 6, 6, 700)
				.setChargingHandler(ChargingManager.REVOLVER).setReloadingHandler(ReloadingManager.SINGLERELOAD).setMaterial(Material.CROSSBOW)
				.setIsSecondaryWeapon(true).setRecoil(10).done();
		GunYMLCreator
				.createNewDefaultGun(dataFolder, "awp", "AWP", 20, stringsMetalRif, WeaponType.SNIPER,
						WeaponSounds.GUN_BIG, true, "762", 10, 12, 3000)
			.setUseOffhand(true)	.setDelayShoot(0.8).setZoomLevel(9).setSwayMultiplier(3).setRecoil(5).setMaterial(Material.CROSSBOW).done();

		GunYMLCreator.createMisc(false, dataFolder, false, "default_smokegrenade", "smokegrenade",
				"&7Smoke Grenade",
				Arrays.asList(ChatColor.DARK_GRAY + "[LMB] to pull pin", ChatColor.DARK_GRAY + "[RMB] to throw",
						ChatColor.DARK_GRAY + "Smoke Grenades wait " + ChatColor.GRAY + "FIVE seconds"
								+ ChatColor.DARK_GRAY + " before exploding.",
						ChatColor.DARK_RED + "<!>Will Explode Even If Not Thrown<!>"),
				m(21), stringsGrenades, 100, WeaponType.SMOKE_GRENADES, 100, 1).set(false, "radius", 5).done();
		GunYMLCreator.createMisc(false, dataFolder, false, "default_flashbang", "flashbang", "&7FlashBang",
				Arrays.asList(ChatColor.DARK_GRAY + "[LMB] to pull pin", ChatColor.DARK_GRAY + "[RMB] to throw",
						ChatColor.DARK_GRAY + "Flashbangs wait " + ChatColor.GRAY + "FIVE seconds"
								+ ChatColor.DARK_GRAY + " before exploding.",
						ChatColor.DARK_RED + "<!>Will Explode Even If Not Thrown<!>"),
				m(22), stringsGrenades, 100, WeaponType.FLASHBANGS, 100, 1).set(false, "radius", 5).done();

		GunYMLCreator
				.createAttachment(false, dataFolder, false, "default_p30_silencer", "p30silenced",
						ChatColor.GOLD + "P30[Silenced]", null, m(23), stringsPistol, 1000, "p30")
				.setWeaponSound(WeaponSounds.SILENCEDSHOT).setMaterial(Material.CROSSBOW).dontVerify().done();
		GunYMLCreator
				.createAttachment(false, dataFolder, false, "default_awp_asiimov", "awpasiimov",
						ChatColor.GOLD + "AWP[Asiimov-skin]", null, m(24), stringsMetalRif, 1000, "awp").setMaterial(Material.CROSSBOW)
				.setUseOffhand(true).dontVerify().done();

		GunYMLCreator
				.createNewDefaultGun(dataFolder, "m4a1s", "M4A1s", 25, stringsMetalRif, WeaponType.RIFLE,
						null, true, "556", 3, 30, 3600)
				.setFullyAutomatic(2).setBulletsPerShot(1).setRecoil(5).setMaterial(Material.CROSSBOW).done();

		GunYMLCreator
				.createNewDefaultGun(dataFolder, "rpk", "RPK", 26, stringsWoodRif, WeaponType.RIFLE, null,
						false, "762", 3, 70, 7000)
				.setFullyAutomatic(3).setBulletsPerShot(1).setRecoil(2).setMaterial(Material.CROSSBOW).done();

		GunYMLCreator
				.createNewDefaultGun(dataFolder, "sg553", "SG-553", 27, stringsMetalRif, WeaponType.RIFLE,
						null, true, "556", 3, 40, 3200)
				.setFullyAutomatic(2).setBulletsPerShot(1).setRecoil(2).setMaterial(Material.CROSSBOW).done();
		GunYMLCreator.createNewDefaultGun(dataFolder, "fnfiveseven", "FN-Five-Seven", 47, stringsPistol,
				WeaponType.PISTOL, null, true, "9mm", 3, 12, 700).setMaterial(Material.CROSSBOW).setIsSecondaryWeapon(true).done();

		GunYMLCreator
				.createNewDefaultGun(dataFolder, "dp27", "DP-27", 28, stringsMetalRif, WeaponType.RIFLE,
						WeaponSounds.GUN_BIG, true, "762", 3, 47, 3000)
				.setFullyAutomatic(2).setBulletsPerShot(1).setRecoil(2).setMaterial(Material.CROSSBOW).done();

		GunYMLCreator
				.createMisc(false, dataFolder, false, "default_incendarygrenade", "incendarygrenade",
						"&7Incendary Grenade",
						Arrays.asList(ChatColor.DARK_GRAY + "[LMB] to pull pin",
								ChatColor.DARK_GRAY + "[RMB] to throw",
								ChatColor.DARK_GRAY + "Incendary Grenades wait " + ChatColor.GRAY
										+ "FIVE seconds" + ChatColor.DARK_GRAY + " before exploding.",
								ChatColor.DARK_RED + "<!>Will Explode Even If Not Thrown<!>"),
						m(29), stringsGrenades, 100, WeaponType.INCENDARY_GRENADES, 100, 1)
				.set(false, "radius", 5).done();
		GunYMLCreator
				.createNewDefaultGun(dataFolder, "homingrpg", "&6Homing RPG Launcher", 30, stringsMetalRif,
						WeaponType.RPG, null, false, "rocket", 100, 1, 5000)
				.setDelayShoot(1).setCustomProjectile(ProjectileManager.HOMING_RPG)
				.setCustomProjectileExplosionRadius(10).setCustomProjectileVelocity(2)// .setChargingHandler(ChargingManager.HOMINGRPG)
				.setReloadingHandler(ReloadingManager.SINGLERELOAD).setDistance(800).setNightVisionOnScope(true)
				.setParticle("SMOKE_LARGE").setRecoil(10).setMaterial(Material.CROSSBOW).done();

		GunYMLCreator
				.createNewDefaultGun(dataFolder, "flintlockpistol", "\"Harper's Ferry\" Flintlock Pistol",
						31, stringsMetalRif, WeaponType.RIFLE, WeaponSounds.GUN_AUTO, true, "musketball", 10, 1,
						100).setMaterial(Material.CROSSBOW)
				.setSway(0.4).setDelayReload(4).setDelayShoot(1).setIsSecondaryWeapon(true).setRecoil(8).done();

		// Jump for armor

		/**
		 * (boolean forceUpdate, File dataFolder, boolean invalid, String filename,
		 * String name, String displayname, List<String> lore, int id, List<String>
		 * craftingRequirements, WeaponType weapontype, boolean enableIronSights, String
		 * ammotype, int damage, double sway, Material type, int maxBullets, int
		 * duribility, double delayReload, double delayShoot, int bulletspershot,
		 * boolean isAutomatic, int cost, ChargingHandlerEnum ch, int distance, int var,
		 * boolean version18, WeaponSounds ws, String particle, double particleR, double
		 * particleG, double particleB, boolean addMuzzleSmoke)
		 *
		 */


		List<String> stringsFatman = Arrays.asList(new String[] { getIngString(Material.IRON_INGOT, 0, 32),
				getIngString(Material.REDSTONE, 0, 16), getIngString(Material.BLAZE_POWDER, 0, 8) });

		GunYMLCreator
				.createNewDefaultGun(dataFolder, "lazerrifle", "&6Lazer Rifle", 32, stringsMetalRif,
						WeaponType.LAZER, WeaponSounds.LAZERSHOOT, false, "fusion_cell", 4, 20, 2000)
				.setAutomatic(true).setParticle(1, 0, 0, Material.REDSTONE_BLOCK).setDistance(150).setSwayMultiplier(3).setSway(0.2)
				.setRecoil(0).setMaterial(Material.CROSSBOW).done();
		GunYMLCreator
				.createNewDefaultGun(dataFolder, "fatman", "&6Fatman", 33, stringsFatman, WeaponType.RPG,
						WeaponSounds.WARHEAD_LAUNCH, false, "mininuke", 500, 1, 6000)
				.setDelayShoot(1).setCustomProjectile(ProjectileManager.MINI_NUKE)
				.setCustomProjectileExplosionRadius(10).setCustomProjectileVelocity(3)// .setChargingHandler(ChargingManager.MININUKELAUNCHER)
				.setReloadingHandler(ReloadingManager.SINGLERELOAD).setDistance(500).setParticle(0.3, 0.9, 0.3, Material.COAL_BLOCK).setMaterial(Material.CROSSBOW)
				.setRecoil(5).done();

		GunYMLCreator.createNewDefaultGun(dataFolder, "10mmpistol", "&610mm Pistol", 34, strings10mm,
				WeaponType.PISTOL, null, true, "9mm", 3, 12, 700).setIsSecondaryWeapon(true).setMaterial(Material.CROSSBOW).done();

		GunYMLCreator
				.createNewDefaultGun(dataFolder, "instituterifle", "&6Institute Rifle", 35,
						stringsMetalRif, WeaponType.LAZER, WeaponSounds.LAZERSHOOT, false, "fusion_cell", 4, 20,
						2000).setMaterial(Material.CROSSBOW)
				.setAutomatic(true).setParticle(0.5, 0.9, 0.9, Material.LAPIS_BLOCK).setDistance(150).setSwayMultiplier(3)
				.setSway(0.2).setRecoil(0).done();

		GunYMLCreator
				.createNewDefaultGun(dataFolder, "musket", "\"Brown Bess\" Musket", 37, stringsMetalRif,
						WeaponType.RIFLE, WeaponSounds.GUN_AUTO, true, "musketball", 10, 1, 100)
				.setSway(0.3).setDelayReload(5).setDelayShoot(1).setSwayMultiplier(3).setRecoil(3).setMaterial(Material.CROSSBOW).done();

		List<String> stringsRifle = Arrays.asList(new String[] { getIngString(Material.IRON_INGOT, 0, 8),
				getIngString(Material.REDSTONE, 0, 3) });
		List<String> stringsLight = Arrays.asList(new String[] { getIngString(Material.IRON_INGOT, 0, 8),
				getIngString(Material.NETHER_STAR, 0, 1) });

		GunYMLCreator
				.createNewCustomGun(dataFolder, "default_aliensrifle", "m41pulserifle",
						ChatColor.GOLD + "M41PulseRifle", 37, stringsRifle, WeaponType.RIFLE,
						WeaponSounds.GUN_MEDIUM, false, "556", 4, 30, 5000)
				.setLore(Collections.singletonList("&fGame over, man. Game over!")).setFullyAutomatic(3)
				.setBulletsPerShot(1).setMuzzleSmoke(false).setRecoil(2).setMaterial(Material.CROSSBOW).done();
		GunYMLCreator
				.createNewCustomGun(dataFolder, "default_auto9", "auto9", ChatColor.GOLD + "Auto9", 38,
						stringsPistol, WeaponType.PISTOL, WeaponSounds.GUN_DEAGLE, true, "556", 5, 12, 700)
				.setLore(Collections.singletonList("&fDead or alive, you're coming with me! ")).setMaterial(Material.CROSSBOW).setZoomLevel(1)
				.setRecoil(2).done();
		GunYMLCreator
				.createNewCustomGun(dataFolder, "default_arcgun9", "arcgun9",
						ChatColor.GOLD + "The Arc-Gun-9", 39, strings10mm, WeaponType.LAZER,
						WeaponSounds.SHOCKWAVE, false, "fusion_cell", 0, 10, 2400)
				.setLore(Collections.singletonList("&fPushy!")).setMaterial(Material.CROSSBOW)
				.setChargingHandler(ChargingManager.getHandler(ChargingManager.PUSHBACK)).done();
		GunYMLCreator
				.createNewCustomGun(dataFolder, "default_halorifle", "unscassaultrifle",
						ChatColor.GOLD + "UNSCAssaultRifle", 40, stringsRifle, WeaponType.RIFLE,
						WeaponSounds.GUN_MEDIUM, true, "556", 3, 32, 3800)
				.setFullyAutomatic(3).setBulletsPerShot(1).setMaterial(Material.CROSSBOW)
				.setLore(Collections.singletonList("&fAlso known as the \"MA5B\"")).setRecoil(2).done();
		GunYMLCreator
				.createNewCustomGun(dataFolder, "default_haloalien", "alienneedler",
						ChatColor.GOLD + "\"Needler\"", 41, stringsRifle, WeaponType.PISTOL,
						WeaponSounds.GUN_NEEDLER, true, "fusion_cell", 1, 32, 2000)
				.setFullyAutomatic(4).setBulletsPerShot(1).setMaterial(Material.CROSSBOW)
				.setLore(Collections.singletonList("&fWarning: Sharp")).setParticle("REDSTONE", 1, 0.1, 1, Material.DIAMOND_BLOCK)
				.done();
		GunYMLCreator
				.createNewCustomGun(dataFolder, "default_thatgun", "thatgun",
						ChatColor.GOLD + "\"That Gun\"", 42, stringsRifle, WeaponType.PISTOL,
						WeaponSounds.GUN_DEAGLE, true, "556", 5, 12, 2000)
				.setLore(Collections.singletonList("&fAlso known as the \"LAPD 2019 Detective Special\"")).setMaterial(Material.CROSSBOW)
				.setRecoil(2).done();
		GunYMLCreator
				.createNewCustomGun(dataFolder, "default_blaster", "blaster",
						ChatColor.GOLD + "\"Blaster\" Pistol", 43, stringsGoldRif, WeaponType.LAZER,
						WeaponSounds.GUN_STARWARS, false, "fusion_cell", 4, 20, 1600)
				.setFullyAutomatic(1).setBulletsPerShot(1).setMuzzleSmoke(false).setParticle(1, 0, 0, Material.REDSTONE_BLOCK)
				.setLore(Collections.singletonList("&fMiss all the shots you want!")).setRecoil(0).setMaterial(Material.CROSSBOW).done();
		GunYMLCreator
				.createNewCustomGun(dataFolder, "default_hl2pulserifle", "pulserifle",
						ChatColor.GOLD + "Overwatch Pulse Rifle", 44, stringsGoldRif, WeaponType.LAZER,
						WeaponSounds.GUN_HALOLAZER, true, "fusion_cell", 4, 30, 5000)
				.setFullyAutomatic(3).setBulletsPerShot(1).setMuzzleSmoke(false).setMaterial(Material.CROSSBOW)
				.setLore(Collections.singletonList("&fStardard Issue Rifles for Combie solders."))
				.setParticle(0.5, 0.99, 0.99, Material.GOLD_BLOCK).setRecoil(2).done();
		GunYMLCreator
				.createNewCustomGun(dataFolder, "default_vera", "vera", ChatColor.GOLD + "Vera", 45,
						stringsGoldRif, WeaponType.RIFLE, WeaponSounds.GUN_DEAGLE, true, "556", 3, 30, 3000)
				.setUseOffhand(true).setNightVisionOnScope(true).setZoomLevel(5).setMaterial(Material.CROSSBOW)
				.setLore(Arrays.asList("&fThe Callahan Full-bore Auto-lock.", "&7\"Customized trigger, �",
						"&7double cartridge thorough gauge.", "&7It is my very favorite gun ",
						"&7This is the best gun made by man.", "&7 It has extreme sentimental value ",
						"&7I call her Vera.\"-Jayne Cobb"))
				.setFullyAutomatic(2).setBulletsPerShot(1).setRecoil(2).done();

		GunYMLCreator.createMisc(false, dataFolder, false, "default_lightsaberblue", "LightSaberBlue",
				ChatColor.GOLD + "(Blue)LightSaber", Arrays.asList("&fMay The Force be with you", "&fAlways"),
				Material.CROSSBOW, 46, stringsLight, 10000, WeaponType.MEELEE, 9, 1000);
		GunYMLCreator.createMisc(false, dataFolder, false, "default_lightsaberred", "LightSaberRed",
				ChatColor.GOLD + "(Red)LightSaber", Arrays.asList("&fMay The Force be with you", "&fAlways"),
				Material.CROSSBOW, 47, stringsLight, 10000, WeaponType.MEELEE, 9, 1000);

		GunYMLCreator.createNewDefaultGun(dataFolder, "mac10", "Mac-10", 48, stringsMetalRif,
				WeaponType.SMG, WeaponSounds.GUN_SMALL_AUTO, true, "9mm", 2, 32, 2500).setFullyAutomatic(3)
				.setMaterial(Material.CROSSBOW).done();
		GunYMLCreator.createNewDefaultGun(dataFolder, "uzi", "UZI", 49, stringsMetalRif, WeaponType.SMG,
				WeaponSounds.GUN_SMALL_AUTO, true, "9mm", 2, 25, 2000).setFullyAutomatic(3).setMaterial(Material.CROSSBOW).done();
		GunYMLCreator
				.createNewDefaultGun(dataFolder, "skorpion", "Skorpion vz.61", 50, stringsMetalRif,
						WeaponType.SMG, WeaponSounds.GUN_SMALL_AUTO, true, "9mm", 2, 20, 1600).setMaterial(Material.CROSSBOW)
				.setFullyAutomatic(3).done();

		GunYMLCreator
				.createNewDefaultGun(dataFolder, "sks", "SKS-45", 51, stringsWoodRif, WeaponType.SNIPER,
						null, true, "762", 7, 10, 2000).setMaterial(Material.CROSSBOW)
			.setUseOffhand(true)	.setDelayShoot(0.6).setZoomLevel(6).setDistance(290).setSwayMultiplier(3).setRecoil(8).done();

		GunYMLCreator
				.createNewDefaultGun(dataFolder, "barrett", "Barrett-M82", 52, stringsWoodRif,
						WeaponType.SNIPER, WeaponSounds.GUN_BIG, true, "50bmg", 17, 10, 4000)
				.setDelayShoot(1).setZoomLevel(6).setDistance(350).setDelayReload(2.5).setSwayMultiplier(3).setMaterial(Material.CROSSBOW).setUseOffhand(true)
				.setNightVisionOnScope(true).setRecoil(15).done();

		GunYMLCreator
				.createNewDefaultGun(dataFolder, "makarov", "Makarov \"PM\"", 53, stringsPistol,
						WeaponType.PISTOL, WeaponSounds.GUN_SMALL_AUTO, true, "9mm", 3, 8, 700).setMaterial(Material.CROSSBOW)
				.setIsSecondaryWeapon(true).done();

		GunYMLCreator.createNewDefaultGun(dataFolder, "ppsh41", "PPSh-41", 54, stringsWoodRif,
				WeaponType.RIFLE, null, true, "762", 3, 71, 7000).setFullyAutomatic(3).setRecoil(2).setMaterial(Material.CROSSBOW).done();
		GunYMLCreator
				.createNewDefaultGun(dataFolder, "m79", "&6M79 \"Thumper\"", 55, stringsFatman,
						WeaponType.RPG, WeaponSounds.WARHEAD_LAUNCH, true, "40mm", 100, 1, 5000)
				.setDelayShoot(1).setCustomProjectile(ProjectileManager.EXPLODINGROUND)
				.setCustomProjectileVelocity(2).setCustomProjectileExplosionRadius(6)// .setChargingHandler(ChargingManager.MININUKELAUNCHER)
				.setReloadingHandler(ReloadingManager.SINGLERELOAD).setDistance(500).setMaterial(Material.CROSSBOW)
				.setParticle(0.001, 0.001, 0.001, Material.COAL_BLOCK).setRecoil(10).done();
		GunYMLCreator
				.createNewDefaultGun(dataFolder, "minigun", "Minigun", 56, stringsMetalRif,
						WeaponType.BIG_GUN, WeaponSounds.GUN_BIG, true, "556", 2, 200, 15000)
				.setFullyAutomatic(5).setBulletsPerShot(1).setChargingHandler(ChargingManager.REQUIREAIM).setMaterial(Material.CROSSBOW)
				.setSway(0.5).setSwayMultiplier(2.4).setUseOffhand(true).setParticle(0.9, 0.9, 0.9, Material.STONE).done();
		GunYMLCreator// TODO: MINIGUN RECOIL
				.createNewDefaultGun(dataFolder, "mk19", "Mk-19", 57, stringsMetalRif, WeaponType.BIG_GUN,
						WeaponSounds.WARHEAD_LAUNCH, true, "40mm", 50, 50, 20000)
				.setFullyAutomatic(1).setCustomProjectile(ProjectileManager.EXPLODINGROUND)
				.setCustomProjectileVelocity(4).setCustomProjectileExplosionRadius(5)
			.setUseOffhand(true)	.setChargingHandler(ChargingManager.REQUIREAIM).setSway(0.5).setSwayMultiplier(2.4).setMaterial(Material.CROSSBOW)
				.setParticle(0.001, 0.001, 0.001, Material.COAL_BLOCK).setRecoil(7).done();
		GunYMLCreator
				.createNewDefaultGun(dataFolder, "asval", "AS-Val", 58, stringsMetalRif, WeaponType.RIFLE,
						WeaponSounds.SILENCEDSHOT, true, "762", 3, 30, 7000).setMaterial(Material.CROSSBOW)
				.setUseOffhand(true).setSway(0.2).setFullyAutomatic(3).setRecoil(2).done();
		GunYMLCreator
				.createNewDefaultGun(dataFolder, "fnp90", "FN-P90", 59, stringsMetalRif, WeaponType.SMG,
						WeaponSounds.SILENCEDSHOT, true, "556", 2, 50, 3000).setMaterial(Material.CROSSBOW)
				.setDelayReload(2.5).setFullyAutomatic(4).setRecoil(2).done();
		GunYMLCreator
				.createNewDefaultGun(dataFolder, "kar98k", "Kar-98K", 60, stringsWoodRif,
						WeaponType.SNIPER, null, true, "762", 10, 6, 2500).setMaterial(Material.CROSSBOW)
			.setUseOffhand(true)	.setZoomLevel(2).setDelayShoot(0.7).setChargingHandler(ChargingManager.BOLT)
				.setSwayMultiplier(3).setDistance(280).setRecoil(7).done();
		GunYMLCreator.createNewDefaultGun(dataFolder, "mp40", "MP 40", 61, stringsMetalRif,
				WeaponType.SMG, WeaponSounds.GUN_SMALL, true, "9mm", 2, 32, 3800).setFullyAutomatic(3).setMaterial(Material.CROSSBOW).done();
		GunYMLCreator
				.createNewDefaultGun(dataFolder, "sturmgewehr44", "Sturmgewehr 44", 62, stringsMetalRif,
						WeaponType.SMG, WeaponSounds.GUN_AUTO, true, "762", 3, 30, 3800).setMaterial(Material.CROSSBOW)
				.setFullyAutomatic(3).setRecoil(2).done();

		/**
		 * Variant systems
		 */
		GunYMLCreator
				.createNewDefaultGun(dataFolder, "vz58", "VZ.58", 4, stringsMetalRif, WeaponType.RIFLE,
						null, true, "762", 3, 30, 4500)
				.setSway(0.2).setFullyAutomatic(2).setBulletsPerShot(1).setVariant(1).setMaterial(Material.CROSSBOW).done();
		GunYMLCreator.createNewDefaultGun(dataFolder, "cz65", "CZ.75", 2, stringsPistol, WeaponType.PISTOL,
				null, true, "9mm", 3, 12, 700).setIsSecondaryWeapon(true).setVariant(1).setMaterial(Material.CROSSBOW).done();

		GunYMLCreator
				.createNewDefaultGun(dataFolder, "sawedoffshotgun", "Sawed-off Shotgun", 63,
						stringsMetalRif, WeaponType.SHOTGUN, null, true, "shell", 2, 2, 1000)
				.setReloadingHandler(ReloadingManager.SINGLERELOAD).setDelayReload(1).setBulletsPerShot(20).setMaterial(Material.CROSSBOW)
				.setDistance(80).setRecoil(11).done();
		GunYMLCreator
				.createNewDefaultGun(dataFolder, "famas", "FAMAS-G2", 64, stringsMetalRif,
						WeaponType.RIFLE, null, true, "556", 3, 30, 4500).setMaterial(Material.CROSSBOW)
				.setFullyAutomatic(3).setRecoil(2).done();

		/*GunYMLCreator.createDefaultArmor(dataFolder, false, "assaulthelmet", "Assault Helmet", null, 25,
				stringsHelmet, 3000, WeaponType.HELMET, 1.5, 2, true);
		GunYMLCreator.createDefaultArmor(dataFolder, false, "ncrhelmet", "NCR Ranger Helmet", null, 59,
				stringsHelmet, 5000, WeaponType.HELMET, 1.5, 2, true);
		GunYMLCreator.createDefaultArmor(dataFolder, false, "skimask", "Ski Mask", null, 60, StringsWool,
				50, WeaponType.HELMET, 1, 0, false);
		GunYMLCreator.createDefaultArmor(dataFolder, false, "ushanka", "Ushanka-Hat", null, 61,
				StringsWool, 50, WeaponType.HELMET, 1, 0, false);
		*/
		GunYMLCreator
				.createNewDefaultGun(dataFolder, "flamer", "Flamer", 65, stringsMetalRif,
						WeaponType.FLAMER, WeaponSounds.HISS, false, "fuel", 1, 60, 8000)
				.setFullyAutomatic(5).setRecoil(0).setCustomProjectile(ProjectileManager.FIRE).setDistance(11).setMaterial(Material.CROSSBOW)
				.done();
		GunYMLCreator
				.createNewDefaultGun(dataFolder, "glock", "Glock-17", 1, stringsPistol, WeaponType.PISTOL,
						null, true, "9mm", 3, 15, 1800)
				.setIsSecondaryWeapon(true).setFireRate(3).setVariant(2).setMaterial(Material.CROSSBOW).done();

		GunYMLCreator
				.createNewDefaultGun(dataFolder, "sten", "STEN Gun", 66, stringsMetalRif,
						WeaponType.RIFLE, WeaponSounds.GUN_SMALL_AUTO, true, "9mm", 2, 32, 2500)
				.setFullyAutomatic(3).setRecoil(1).setMaterial(Material.CROSSBOW).done();

		GunYMLCreator
				.createNewDefaultGun(dataFolder, "m4a1sburst", "M4A1s (Burst)", 25, stringsMetalRif,
						WeaponType.RIFLE, null, true, "556", 3, 30, 3600)
				.setVariant(1).setChargingHandler(ChargingManager.BURSTFIRE).setFireRate(3).setBulletsPerShot(3).setMaterial(Material.CROSSBOW)
				.setRecoil(1).done();

		GunYMLCreator
				.createNewDefaultGun(dataFolder, "m32a1", "&6M32A1", 67, stringsFatman,
						WeaponType.RPG, WeaponSounds.WARHEAD_LAUNCH, true, "40mm", 100, 6, 60000)
				.setDelayShoot(0.7).setCustomProjectile(ProjectileManager.EXPLODINGROUND)
				.setCustomProjectileVelocity(2).setCustomProjectileExplosionRadius(6)// .setChargingHandler(ChargingManager.MININUKELAUNCHER)
				.setReloadingHandler(ReloadingManager.SINGLERELOAD).setDelayReload(5).setDistance(500).setMaterial(Material.CROSSBOW)
				.setParticle(0.001, 0.001, 0.001, Material.COAL_BLOCK).setRecoil(8).done();




	}

	@Override
	public void initIronSights(File dataFolder) {
		File ironsights = new File(dataFolder,"default_ironsightstoggleitem.yml");
		YamlConfiguration ironconfig = YamlConfiguration.loadConfiguration(ironsights);
		if(!ironconfig.contains("material")){
			ironconfig.set("material",Material.CROSSBOW.name());
			ironconfig.set("id",68);
			try {
				ironconfig.save(ironsights);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		IronsightsHandler.ironsightsMaterial = Material.matchMaterial(ironconfig.getString("material"));
		IronsightsHandler.ironsightsData = ironconfig.getInt("id");
	}

	public String getIngString(Material m, int durability, int amount) {
		return m.toString() + "," + durability + "," + amount;
	}
	public static MaterialStorage m(int d) {
		return MaterialStorage.getMS(Material.CROSSBOW, d, 0);
	}


	private AbstractItemFact fact = new ItemFactory();

	@Override
	public AbstractItemFact getItemFactory() {
		return fact;
	}
}
