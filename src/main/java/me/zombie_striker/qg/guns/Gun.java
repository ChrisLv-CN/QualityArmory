package me.zombie_striker.qg.guns;

import me.zombie_striker.customitemmanager.CustomItemManager;
import me.zombie_striker.customitemmanager.OLD_ItemFact;
import me.zombie_striker.qg.ArmoryBaseObject;
import me.zombie_striker.qg.QAMain;
import me.zombie_striker.customitemmanager.MaterialStorage;
import me.zombie_striker.qg.ammo.*;
import me.zombie_striker.qg.api.QAWeaponPrepareShootEvent;
import me.zombie_striker.qg.api.QualityArmory;
import me.zombie_striker.qg.guns.projectiles.ProjectileManager;
import me.zombie_striker.qg.guns.projectiles.RealtimeCalculationProjectile;
import me.zombie_striker.qg.guns.utils.GunRefillerRunnable;
import me.zombie_striker.qg.guns.utils.GunUtil;
import me.zombie_striker.qg.guns.utils.WeaponSounds;
import me.zombie_striker.qg.guns.utils.WeaponType;
import me.zombie_striker.qg.handlers.Update19OffhandChecker;
import me.zombie_striker.qg.handlers.chargers.ChargingHandler;
import me.zombie_striker.qg.handlers.chargers.ChargingManager;
import me.zombie_striker.qg.handlers.reloaders.ReloadingHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Gun implements ArmoryBaseObject, Comparable<Gun> {

	private String name;
	private MaterialStorage id;
	private ItemStack[] ing;
	private WeaponType type;
	private boolean hasIronSights;
	private int zoomLevel = 0;
	private Ammo ammotype;
	private double acc;
	private double swaymultiplier = 2;
	private int maxbull;
	private float damage;
	private int durib = 1000;
	private boolean isAutomatic;
	boolean supports18 = false;
	boolean nightVisionOnScope = false;

	private double headshotMultiplier = 2;

	private boolean isPrimaryWeapon = true;

	private boolean useOffhandOverride = false;

	private List<String> extralore = null;
	private String displayname = null;

	private List<String> weaponSounds;
	private double volume = 4;

	double cost = 100;

	private double delayBetweenShots = 0.25;

	private int shotsPerBullet = 1;
	private int firerate = 1;

	private double reloadTime = 1.5;

	private ChargingHandler ch = null;
	private ReloadingHandler rh = null;

	private int maxDistance = 150;

	private Particle particle = null;
	private double particle_r = 1;
	private double particle_g = 1;
	private double particle_b = 1;
	private Material particle_material = Material.COAL_BLOCK;

	private int lightl = 20;

	private boolean enableMuzzleSmoke = false;

	public ChatColor glowEffect = null;

	public boolean unlimitedAmmo = false;

	RealtimeCalculationProjectile customProjectile = null;
	double velocity = 2;
	double explosionRadius = 10;
	double recoil = 1;

	// This refers to the last time a gun was shot by a player, on a per-gun basis.
	// Doing this should prevent players from fast-switching to get around
	// bullet-delays
	public HashMap<UUID, Long> lastshot = new HashMap<>();

	public void copyFrom(Gun g) {
		this.ing = g.ing;
		this.type = g.type;
		this.hasIronSights = g.hasIronSights;
		this.zoomLevel = g.zoomLevel;
		this.ammotype = g.ammotype;
		this.acc = g.acc;
		this.swaymultiplier = g.swaymultiplier;
		this.maxbull = g.maxbull;
		this.damage = g.damage;
		this.durib = g.durib;
		this.isAutomatic = g.isAutomatic;
		this.supports18 = g.supports18;
		this.nightVisionOnScope = g.nightVisionOnScope;
		this.headshotMultiplier = g.headshotMultiplier;
		this.isPrimaryWeapon = g.isPrimaryWeapon;
		this.explosionRadius = g.explosionRadius;
		this.extralore = g.extralore;
		// this.displayname = displayname;
		this.weaponSounds = g.weaponSounds;
		this.cost = g.cost;
		this.delayBetweenShots = g.delayBetweenShots;
		this.shotsPerBullet = g.shotsPerBullet;
		this.firerate = g.firerate;
		this.ch = g.ch;
		this.rh = g.rh;
		this.maxDistance = g.maxbull;
		this.particle = g.particle;
		this.particle_r = g.particle_r;
		this.particle_g = g.particle_g;
		this.particle_b = g.particle_b;
		this.particle_material = g.particle_material;
		this.lightl = g.lightl;
		this.enableMuzzleSmoke = g.enableMuzzleSmoke;
		this.glowEffect = g.glowEffect;
		this.unlimitedAmmo = g.unlimitedAmmo;
		this.customProjectile = g.customProjectile;
		this.velocity = g.velocity;
		this.recoil = g.recoil;

	}

	/*
	 * @Deprecated public Gun(String name, MaterialStorage id, WeaponType type,
	 * boolean h, Ammo am, double acc, double swaymult, int maxBullets, float
	 * damage, boolean isAutomatic, int durib, WeaponSounds ws, double cost) {
	 * this(name, id, type, h, am, acc, swaymult, maxBullets, damage, isAutomatic,
	 * durib, ws, null, ChatColor.GOLD + name, cost, null); this.ing =
	 * Main.getInstance().getIngredients(name);
	 * 
	 * }
	 * 
	 * @Deprecated public Gun(String name, WeaponType type, boolean h, Ammo am,
	 * double acc, double swaymult, int maxBullets, float damage, boolean
	 * isAutomatic, int durib, WeaponSounds ws, double cost) { this(name, type, h,
	 * am, acc, swaymult, maxBullets, damage, isAutomatic, durib, ws, cost,
	 * Main.getInstance().getIngredients(name)); }
	 */

	@Deprecated
	public Gun(String name, WeaponType type, boolean h, Ammo am, double acc, double swaymult, int maxBullets,
			float damage, boolean isAutomatic, int durib, WeaponSounds ws, double cost, ItemStack[] ing) {
		this(name, type, h, am, acc, swaymult, maxBullets, damage, isAutomatic, durib, ws.getSoundName(), cost, ing);
	}

	@Deprecated
	public Gun(String name, WeaponType type, boolean h, Ammo am, double acc, double swaymult, int maxBullets,
			float damage, boolean isAutomatic, int durib, String ws, double cost, ItemStack[] ing) {
		this.name = name;
		this.type = type;
		this.hasIronSights = h;
		this.ammotype = am;
		this.ing = ing;
		this.acc = acc;
		this.maxbull = maxBullets;
		this.damage = damage;
		this.durib = durib;
		this.swaymultiplier = swaymult;
		this.isAutomatic = isAutomatic;
		this.weaponSounds = new ArrayList<String>();
		this.weaponSounds.add(ws);

		this.cost = cost;
		this.displayname = ChatColor.GOLD + name;
	}

	@Deprecated
	public Gun(String name, MaterialStorage id, WeaponType type, boolean h, Ammo am, double acc, double swaymult,
			int maxBullets, float damage, boolean isAutomatic, int durib, WeaponSounds ws, double cost,
			ItemStack[] ing) {
		this(name, id, type, h, am, acc, swaymult, maxBullets, damage, isAutomatic, durib, ws, null,
				ChatColor.GOLD + name, cost, ing);
	}

	@Deprecated
	public Gun(String name, MaterialStorage id, WeaponType type, boolean h, Ammo am, double acc, double swaymult,
			int maxBullets, float damage, boolean isAutomatic, int durib, WeaponSounds ws, List<String> extralore,
			String displayname, double cost, ItemStack[] ing) {
		this(displayname, id, type, h, am, acc, swaymult, maxBullets, damage, isAutomatic, durib, ws.getSoundName(),
				extralore, displayname, cost, ing);
	}

	@Deprecated
	public Gun(String name, MaterialStorage id, WeaponType type, boolean h, Ammo am, double acc, double swaymult,
			int maxBullets, float damage, boolean isAutomatic, int durib, String ws, List<String> extralore,
			String displayname, double cost, ItemStack[] ing) {
		this.name = name;
		this.id = id;
		this.type = type;
		this.hasIronSights = h;
		this.ammotype = am;
		this.ing = ing;
		this.acc = acc;
		this.maxbull = maxBullets;
		this.damage = damage;
		this.durib = durib;
		this.swaymultiplier = swaymult;
		this.isAutomatic = isAutomatic;
		this.weaponSounds = new ArrayList<String>();
		this.weaponSounds.add(ws);

		this.cost = cost;

		this.extralore = extralore;
		this.displayname = ChatColor.translateAlternateColorCodes('&', displayname);
	}

	public Gun(String name, MaterialStorage id) {
		this.name = name;
		this.id = id;
	}

	public void setIngredients(ItemStack[] ing) {
		this.ing = ing;
	}

	public void setAutomatic(boolean automatic) {
		this.isAutomatic = automatic;
	}

	public void setHasIronsights(boolean b) {
		this.hasIronSights = b;
	}

	public void setDamage(float damage) {
		this.damage = damage;
	}

	public void setPrice(double price) {
		this.cost = price;
	}

	public void setDuribility(int durib) {
		this.durib = durib;
	}

	public void setMaxBullets(int amount) {
		this.maxbull = amount;
	}

	public void setSway(double sway) {
		this.acc = sway;
	}

	public void setSwayMultiplier(double multiplier) {
		this.swaymultiplier = multiplier;
	}

	public void setHeadshotMultiplier(double dam) {
		headshotMultiplier = dam;
	}

	public double getHeadshotMultiplier() {
		return headshotMultiplier;
	}

	public ChatColor getGlow() {
		return glowEffect;
	}

	public void setLightOnShoot(int level) {
		lightl = level;
	}

	public int getLightOnShoot() {
		return lightl;
	}

	public double getRecoil() {
		return recoil;
	}

	public void setRecoil(double d) {
		this.recoil = d;
	}

	public void setOffhandOverride(boolean b){useOffhandOverride=b;}
	public boolean isOffhandOverride(){return useOffhandOverride;}

	public void setVolume(double f){this.volume = f;}

	public double getVolume(){return volume;}

	/**
	 * Sets the glow for the item. Null to disable the glow.
	 */
	public void setGlow(ChatColor glow) {
		this.glowEffect = glow;
	}

	public double getReloadTime() {
		return reloadTime;
	}

	public void setReloadingTimeInSeconds(double time) {
		this.reloadTime = time;
	}

	public void setBulletsPerShot(int i) {
		this.shotsPerBullet = i;
	}

	public void setNightVision(boolean nightVisionOnScope) {
		this.nightVisionOnScope = nightVisionOnScope;
	}

	public void setAmmo(Ammo ammo) {
		this.ammotype = ammo;
	}

	public boolean hasnightVision() {
		return nightVisionOnScope;
	}

	public boolean usesCustomProjctiles() {
		return customProjectile != null;
	}

	public void setCustomProjectile(String key) {
		this.customProjectile = ProjectileManager.getHandler(key);
	}

	public RealtimeCalculationProjectile getCustomProjectile() {
		return customProjectile;
	}

	public void setRealtimeVelocity(double velocity) {
		this.velocity = velocity;
	}

	public double getVelocityForRealtimeCalculations() {
		return velocity;
	}

	public double getExplosionRadius() {
		return explosionRadius;
	}

	public void setExplosionRadius(double d) {
		this.explosionRadius = d;
	}

	public int getBulletsPerShot() {
		return shotsPerBullet;
	}

	public void setZoomLevel(int zoom) {
		this.zoomLevel = zoom;
	}

	public int getZoomWhenIronSights() {
		return zoomLevel;
	}

	public void setFireRate(int firerate) {
		this.firerate = firerate;
	}

	public int getFireRate() {
		return firerate;
	}

	public void setDisplayName(String displayname) {
		this.displayname = displayname;
	}

	public WeaponType getWeaponType() {
		return type;
	}

	public boolean isPrimaryWeapon() {
		return isPrimaryWeapon;
	}

	public void setIsPrimary(boolean isPrimary) {
		this.isPrimaryWeapon = isPrimary;
	}

	@Override
	public double cost() {
		return cost;
	}

	public boolean isAutomatic() {
		return isAutomatic;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public ItemStack[] getIngredients() {
		return ing;
	}

	@Override
	public int getCraftingReturn() {
		return 1;
	}

	public void setUnlimitedAmmo(boolean b) {
		this.unlimitedAmmo = b;
	}

	public boolean shoot(Player player, boolean onLMB) {
		return Gun.USE_THIS_INSTEAD_OF_INDIVIDUAL_SHOOT_METHODS(this, player, onLMB, getSway());
	}

	@SuppressWarnings("deprecation")
	public static boolean USE_THIS_INSTEAD_OF_INDIVIDUAL_SHOOT_METHODS(Gun g, Player player, boolean onLMB, double acc) {
		boolean offhand = QualityArmory.isIronSights(player.getInventory().getItemInHand());
		if ((!offhand && getAmount(player.getInventory().getItemInHand()) > 0)
				|| (offhand && Update19OffhandChecker.hasAmountOFfhandGreaterthan(player, 0))) {
			QAWeaponPrepareShootEvent shootevent = new QAWeaponPrepareShootEvent(player, g);
			Bukkit.getPluginManager().callEvent(shootevent);
			if (shootevent.isCanceled()) {
				return false;
			}
			GunUtil.basicShoot(offhand, g, player, onLMB, acc);
			return true;
		}
		return false;
	}

	public int getMaxBullets() {
		return maxbull;
	}

	public boolean playerHasAmmo(Player player) {
		if (player.getGameMode() == GameMode.CREATIVE)
			return true;
		if (hasUnlimitedAmmo())
			return true;
		if (getAmmoType() == null)
			return true;
		return GunUtil.hasAmmo(player, this);
	}

	public void setSound(WeaponSounds sound) {
		setSound(sound.getSoundName());
	}

	public void setSound(String sound) {
		this.weaponSounds.clear();
		this.weaponSounds.add(sound);
	}

	public void setSounds(List<String> sound) {
		this.weaponSounds = sound;
	}

	public void reload(Player player) {
		if (getChargingVal() == null || (getReloadingingVal() == null || !getReloadingingVal().isReloading(player)))
			GunUtil.basicReload(this, player, unlimitedAmmo, reloadTime);
	}

	public double getDamage() {
		return damage;
	}

	public int getDurability() {
		return this.durib;
	}

	public Ammo getAmmoType() {
		return ammotype;
	}

	public boolean hasIronSights() {
		return hasIronSights;
	}

	public boolean hasUnlimitedAmmo() {
		if (unlimitedAmmo)
			return true;
		return ammotype == null;
	}

	public double getSway() {
		// TODO Auto-generated method stub
		return acc;
	}

	public double getMovementMultiplier() {
		// TODO Auto-generated method stub
		return swaymultiplier;
	}

	@Override
	public MaterialStorage getItemData() {
		if (id == null) {
			for (Entry<MaterialStorage, Gun> e : QAMain.gunRegister.entrySet()) {
				if (e.getValue() == this)
					return id = e.getKey();
			}
		}
		return id;
	}

	@Deprecated
	public String getWeaponSound() {
		return weaponSounds.get(0);
	}

	public List<String> getWeaponSounds() {
		return weaponSounds;
	}

	@Override
	public List<String> getCustomLore() {
		return extralore;
	}

	@Override
	public String getDisplayName() {
		return displayname;
	}

	public double getDelayBetweenShotsInSeconds() {
		return delayBetweenShots;
	}

	public void setDelayBetweenShots(double seconds) {
		this.delayBetweenShots = seconds;
	}

	public HashMap<UUID, Long> getLastShotForGun() {
		return lastshot;
	}

	public ChargingHandler getChargingVal() {
		return ch;
	}

	public void setChargingHandler(ChargingHandler ch) {
		this.ch = ch;
	}

	public ReloadingHandler getReloadingingVal() {
		return rh;
	}

	public void setReloadingHandler(ReloadingHandler rh) {
		this.rh = rh;
	}

	public int getMaxDistance() {
		return maxDistance;
	}

	public void setMaxDistance(int a) {
		this.maxDistance = a;
	}

	@Override
	public boolean is18Support() {
		return supports18;
	}

	@Override
	public void set18Supported(boolean b) {
		supports18 = b;
	}

	public Particle getParticle() {
		return particle;
	}

	public double getParticleR() {
		return particle_r;
	}

	public double getParticleG() {
		return particle_g;
	}

	public double getParticleB() {
		return particle_b;
	}

	public Material getParticleMaterial() {
		return this.particle_material;
	}

	public void setParticles(Particle p) {
		this.setParticles(p, 1, 1, 1, Material.COAL_BLOCK);
	}

	public void setParticles(Particle p, double r, double g, double b, Material m) {
		particle = p;
		this.particle_r = r;
		this.particle_g = g;
		this.particle_b = b;
		this.particle_material = m;
	}

	public boolean useMuzzleSmoke() {
		return enableMuzzleSmoke;
	}

	public void setUseMuzzleSmoke(boolean b) {
		this.enableMuzzleSmoke = b;
	}

	@Override
	public int compareTo(Gun arg0) {
		if (QAMain.orderShopByPrice) {
			return (int) (this.cost - arg0.cost);
		}
		return this.displayname.compareTo(arg0.displayname);
	}

	// ================= Brain stuck  @o@" =======================
	// Swap left and right button click events should be done in the listener.
	//
	// Left click is always fire the weapon.
	// Right click in the aiming state has the following special circumstances:
	//
	// Fire the weapon:
	// - enableIronSightsON_RIGHT_CLICK = false
	// - reloadOnFOnly = true;
	//
	// ============================================================

	@Override
	public void onRMB(PlayerInteractEvent e, ItemStack usedItem) {
		// right click is default to reload.
		boolean flag = (QAMain.reloadOnFOnly && !QAMain.enableIronSightsON_RIGHT_CLICK);
		QAMain.DEBUG("Gun's RMB click called, fire_flag is " + flag + " Swap R2L " + !QAMain.SWAP_RMB_WITH_LMB);
		onClick(e, usedItem, false, flag);
	}

	@Override
	public void onLMB(PlayerInteractEvent e, ItemStack usedItem) {
		// Left click always fire the gun.
		boolean flag = true;
		QAMain.DEBUG("Gun's LMB click called, fire_flag is " + flag + " Swap R2L " + !QAMain.SWAP_RMB_WITH_LMB);
		onClick(e, usedItem, true, flag);
	}

	@SuppressWarnings("deprecation")
	public void onClick(final PlayerInteractEvent e, ItemStack usedItem, boolean onLMB, boolean fire) {
		QAMain.DEBUG("CLICKED GUN " + getName());

		if (!e.getPlayer().hasPermission("qualityarmory.usegun")) {
			e.getPlayer().sendMessage(QAMain.S_NOPERM);
			e.setCancelled(true);
			return;
		}

		/*
		 * if (QAMain.enableVisibleAmounts) {
		 * QAMain.DEBUG("UNSUPPORTED - Enable visable ammo amount ID check"); boolean
		 * validcheck2 = false; try { if (QAMain.isVersionHigherThan(1, 9)) {
		 * UUID.fromString(usedItem.getItemMeta().getLocalizedName());
		 * QAMain.DEBUG("Gun-Validation check - 1"); } else { validcheck2 = true; } }
		 * catch (Error | Exception e34) { validcheck2 = true; } if (validcheck2) { if
		 * (QAMain.isVersionHigherThan(1, 9)) { if
		 * (!usedItem.getItemMeta().hasDisplayName() ||
		 * !usedItem.getItemMeta().hasLore()) { ItemStack is =
		 * ItemFact.getGun(MaterialStorage.getMS(usedItem)); e.setCancelled(true);
		 * e.getPlayer().setItemInHand(is); QAMain.DEBUG("Gun-Validation check - 2");
		 * return; } } } }
		 */

		QAMain.DEBUG("Dups check");
		QAMain.checkforDups(e.getPlayer(), usedItem);

		ItemStack offhandItem = Update19OffhandChecker.getItemStackOFfhand(e.getPlayer());
		boolean offhand = offhandItem != null && offhandItem.equals(usedItem);

		// AttachmentBase attachment =
		// me.zombie_striker.qg.api.QualityArmory.getGunWithAttchments(usedItem);
		/*
		 * Gun g = QualityArmory.getGun(usedItem); if (g == null) g =
		 * attachment.getBaseGun();
		 */
		QAMain.DEBUG("Made it to gun/attachment check : " + getName());
		try {
			if (QAMain.enableInteractChests) {
				if (e.getClickedBlock() != null
						&& (e.getClickedBlock().getType() == Material.CHEST
						|| e.getClickedBlock().getType() == Material.TRAPPED_CHEST)
						|| e.getClickedBlock().getType() == Material.ENDER_CHEST) {
					QAMain.DEBUG("Chest interactable check has return true!");
					return;
				}
			}
		}catch (Error|Exception e4){}

		e.setCancelled(true);
		if (fire) {
			QAMain.DEBUG("Fire mode called");
			if (!QAMain.enableDurability /*|| OLD_ItemFact.getDamage(usedItem) > 0*/||true) {
				// if (allowGunsInRegion(e.getPlayer().getLocation())) {
				try {
					if (e.getHand() == EquipmentSlot.OFF_HAND) {
						QAMain.DEBUG("OffHandChecker was disabled for shooting!");
						return;
					}
				} catch (Error | Exception e4) {
				}
				// check reloading
				if (e.getPlayer().getItemInHand().getItemMeta().getDisplayName().contains(QAMain.S_RELOADING_MESSAGE)) {
					if (!GunRefillerRunnable.hasItemReloaded(usedItem)) {
						ItemStack tempused = usedItem.clone();
						ItemMeta im = tempused.getItemMeta();
						im.setDisplayName(getDisplayName());
						tempused.setItemMeta(im);
						if (offhand) {
							Update19OffhandChecker.setOffhand(e.getPlayer(), tempused);
							QAMain.DEBUG("odd. Reloading broke. Removing reloading message from offhand - firing");
						} else {
							e.getPlayer().setItemInHand(tempused);
							QAMain.DEBUG("odd. Reloading broke. Removing reloading message from mainhand - firing");
						}
					}
					QAMain.DEBUG("Reloading message 1!");
					// the gun is reloading, not ready to FIRE.
					return;
				}
				// Fast gunman's Machine gun, check the Ammo.
				if (isAutomatic() && GunUtil.rapidfireshooters.containsKey(e.getPlayer().getUniqueId())) {
					QAMain.DEBUG("Fast gunman holding a machine gun, check the Ammo.");
					GunUtil.rapidfireshooters.remove(e.getPlayer().getUniqueId()).cancel();
					if (QAMain.enableReloadWhenOutOfAmmo) {
						// Check the Ammo
						if (getAmount(usedItem) <= 0) {
							// no Ammo in this gun
							if (offhand) {
								// take off the gun ready to reload the ammo.
								e.getPlayer().setItemInHand(e.getPlayer().getInventory().getItemInOffHand());
								e.getPlayer().getInventory().setItemInOffHand(null);
								usedItem = e.getPlayer().getItemInHand();
								offhand = false;
							}
							if (QAMain.allowGunReload) {
								// Auto reloading
								QualityArmory.sendHotbarGunAmmoCount(e.getPlayer(), this, usedItem,
										((getMaxBullets() != getAmount(usedItem))
												&& GunUtil.hasAmmo(e.getPlayer(), this)));
								if (playerHasAmmo(e.getPlayer())) {
									QAMain.DEBUG("Trying to reload Machine gun WITH AUTORELOAD. player has ammo, calling reload mode.");
									reload(e.getPlayer());
								} else {
									if (QAMain.showOutOfAmmoOnItem) {
										// ItemFact.addOutOfAmmoToDisplayname(g, e.getPlayer(), usedItem, slot);
										QAMain.DEBUG("UNSUPPORTED: Out of ammo displayed on item");
									}
									QAMain.DEBUG("Trying to reload Machine gun WITH AUTORELOAD. player DOES NOT have ammo");
								}
							}
							// the gun is empty.
							return;
						}
						// machine gun has Ammo
						QAMain.DEBUG("Fastman's Machine gun has enough ammo.");
					}
				} else {
					// Normal gunman's normal gun, check the Ammo.
					if (QAMain.enableReloadWhenOutOfAmmo) {
						if (getAmount(usedItem) <= 0) {
							// no Ammo in this rifle
							if (offhand) {
								// take off the gun ready to reload the ammo.
								e.getPlayer().setItemInHand(e.getPlayer().getInventory().getItemInOffHand());
								e.getPlayer().getInventory().setItemInOffHand(null);
								usedItem = e.getPlayer().getItemInHand();
								offhand = false;
							}
							if (QAMain.allowGunReload) {
								// Auto reloading
								QualityArmory.sendHotbarGunAmmoCount(e.getPlayer(), this, usedItem,
										((getMaxBullets() != getAmount(usedItem))
												&& GunUtil.hasAmmo(e.getPlayer(), this)));
								if (playerHasAmmo(e.getPlayer())) {
									QAMain.DEBUG("Trying to reload WITH AUTORELOAD. player has ammo, calling reload mode.");
									reload(e.getPlayer());
								} else {
									QAMain.DEBUG("Trying to reload WITH AUTORELOAD. player DOES NOT have ammo");
								}
							}
							// The gun is empty.
							return;
						}
						// The gun has Ammo.
					}
					QAMain.DEBUG("Normalman's gun has enough ammo and calling shoot.");
					shoot(e.getPlayer(), onLMB);
					if (QAMain.enableDurability) {
						if (QualityArmory.isIronSights(e.getPlayer().getItemInHand())) {
							//Update19OffhandChecker.setOffhand(e.getPlayer(), OLD_ItemFact.damage(this, usedItem));
						} else {
							//e.getPlayer().setItemInHand(OLD_ItemFact.damage(this, usedItem));
						}
					}

				}

				QualityArmory.sendHotbarGunAmmoCount(e.getPlayer(), this, usedItem, false);
				return;
				/*
				 * } else { Main.DEBUG("Worldguard region canceled the event"); }
				 */
				// sendHotbarGunAmmoCount(e.getPlayer(), g, attachment, usedItem, false);
				// TODO: Verify that the gun is in the main hand.
				// Shouldn't work for offhand, but it should still
				// be checked later.
			}
			QAMain.DEBUG("End of fire mode check called");

		} else {
			QAMain.DEBUG("Non-Fire mode activated");
			// just reloading
			// Check if the gun is reloading
			if (usedItem.hasItemMeta()
					&& usedItem.getItemMeta().hasDisplayName()
					&& !usedItem.getItemMeta().getDisplayName().contains(QAMain.S_RELOADING_MESSAGE)) {
				QAMain.DEBUG("Reload called");
				if (e.getClickedBlock() != null && QAMain.interactableBlocks.contains(e.getClickedBlock().getType())) {
					e.setCancelled(false);
				} else {
					if (QAMain.allowGunReload) {
						QualityArmory.sendHotbarGunAmmoCount(e.getPlayer(), this, usedItem,
								((getMaxBullets() != getAmount(usedItem))
										&& GunUtil.hasAmmo(e.getPlayer(), this)));
						if (playerHasAmmo(e.getPlayer())) {
							QAMain.DEBUG("Trying to reload. player has ammo");
							reload(e.getPlayer());
						} else {
							QAMain.DEBUG("Trying to reload. player DOES NOT have ammo");
						}
					}
				}
			}


//			if (QAMain.enableIronSightsON_RIGHT_CLICK) {
//				if (!Update19OffhandChecker.supportOffhand(e.getPlayer())) {
//					QAMain.enableIronSightsON_RIGHT_CLICK = false;
//					QAMain.DEBUG("Offhand checker returned false for the player. Disabling ironsights");
//					return;
//				}
//				// Rest should be okay
//				if (hasIronSights()) {
//					try {
//
//						if (e.getPlayer().getItemInHand().getItemMeta().getDisplayName()
//								.contains(QAMain.S_RELOADING_MESSAGE)) {
//							if (!GunRefillerRunnable.hasItemReloaded(usedItem)) {
//								ItemStack tempused = usedItem.clone();
//								ItemMeta im = tempused.getItemMeta();
//								im.setDisplayName(getDisplayName());
//								tempused.setItemMeta(im);
//								if (offhand) {
//									Update19OffhandChecker.setOffhand(e.getPlayer(), tempused);
//									QAMain.DEBUG(
//											"odd. Reloading broke. Removing reloading message from offhand - reload");
//								} else {
//									e.getPlayer().setItemInHand(tempused);
//									QAMain.DEBUG(
//											"odd. Reloading broke. Removing reloading message from mainhand - reload");
//								}
//							}
//							QAMain.DEBUG("Reloading message 1!");
//							return;
//						}
//						// ItemStack offhandItem =
//						// Update19OffhandChecker.getItemStackOFfhand(e.getPlayer());
//						if (offhandItem != null) {
//							e.getPlayer().getInventory().addItem(offhandItem);
//							Update19OffhandChecker.setOffhand(e.getPlayer(), null);
//						}
//
//						ItemStack tempremove = null;
//						if (e.getPlayer().getInventory().getItemInOffHand() != null)
//							tempremove = e.getPlayer().getInventory().getItemInOffHand();
//						e.getPlayer().getInventory().setItemInOffHand(e.getPlayer().getInventory().getItemInMainHand());
//						if (tempremove != null) {
//							e.getPlayer().getInventory().setItemInMainHand(QualityArmory.getIronSightsItemStack());
//
//							QAMain.toggleNightvision(e.getPlayer(), this, true);
//							QAMain.DEBUG("Toggle Night vision on right click");
//							final Gun checkTo = QualityArmory
//									.getGun(Update19OffhandChecker.getItemStackOFfhand(e.getPlayer()));
//							new BukkitRunnable() {
//
//								@Override
//								public void run() {
//									if (!e.getPlayer().isOnline()) {
//										QAMain.DEBUG("Canceling since player is offline");
//										cancel();
//										return;
//									}
//									Gun g = null;
//									if (!QualityArmory.isIronSights(e.getPlayer().getItemInHand())
//											|| (g = QualityArmory.getGun(
//													Update19OffhandChecker.getItemStackOFfhand(e.getPlayer()))) == null
//											|| g != checkTo) {
//										QAMain.toggleNightvision(e.getPlayer(), checkTo, false);
//										QAMain.DEBUG(
//												"Removing nightvision since either the main hand is not ironsights/ offhand gun is null. : "
//														+ (!QualityArmory.isIronSights(e.getPlayer().getItemInHand()))
//														+ " "
//														+ ((g = QualityArmory.getGun(Update19OffhandChecker
//																.getItemStackOFfhand(e.getPlayer()))) == null)
//														+ " " + (g != checkTo));
//										cancel();
//										return;
//									}
//
//								}
//							}.runTaskTimer(QAMain.getInstance(), 20, 20);
//						}
//
//						QualityArmory.sendHotbarGunAmmoCount(e.getPlayer(), this, usedItem, false);
//					} catch (Error e2) {
//						Bukkit.broadcastMessage(QAMain.prefix
//								+ "Ironsights not compatible for versions lower than 1.8. The server owner should set EnableIronSights to false in the plugin's config");
//					}
//				} else {
//					/*
//					 * if (!Main.enableDurability || ItemFact.getDamage(usedItem) > 0) { // if
//					 * (allowGunsInRegion(e.getPlayer().getLocation())) { g.shoot(e.getPlayer(),
//					 * attachment); if (Main.enableDurability) if (offhand) {
//					 * e.getPlayer().getInventory().setItemInOffHand(ItemFact.damage(g, usedItem));
//					 * } else { e.getPlayer().setItemInHand(ItemFact.damage(g, usedItem)); } // }
//					 * QualityArmory.sendHotbarGunAmmoCount(e.getPlayer(), g, attachment, usedItem,
//					 * false); // TODO: Verify that the gun is in the main // hand. // Shouldn't
//					 * work for offhand, but it should // still // be checked later. }
//					 */
//				}
//
//				QAMain.DEBUG("Ironsights on RMB finished");
//			} else {
//				QAMain.DEBUG("Reload called");
//				if (e.getClickedBlock() != null && QAMain.interactableBlocks.contains(e.getClickedBlock().getType())) {
//					e.setCancelled(false);
//				} else {
//					if (QAMain.allowGunReload) {
//						QualityArmory.sendHotbarGunAmmoCount(e.getPlayer(), this, usedItem,
//								((getMaxBullets() != getAmount(usedItem))
//										&& GunUtil.hasAmmo(e.getPlayer(), this)));
//						if (playerHasAmmo(e.getPlayer())) {
//							QAMain.DEBUG("Trying to reload. player has ammo");
//							reload(e.getPlayer());
//						} else {
//							QAMain.DEBUG("Trying to reload. player DOES NOT have ammo");
//						}
//					}
//				}
//			}
		}
		QAMain.DEBUG("Reached end for gun-check!");
	}

	@Override
	public ItemStack getItemStack() {
		return CustomItemManager.getItemFact("gun").getItem(this.getItemData(),1);
	}


	public static int getAmount(ItemStack is) {
		if (is != null) {
			if (is.hasItemMeta() && is.getItemMeta().hasLore()) {
				for (String lore : is.getItemMeta().getLore()) {
					if (lore.contains(QAMain.S_ITEM_BULLETS)) {
						return Integer.parseInt(lore.split(":")[1].split("/")[0].trim());
					}
				}
				return 0;
			}
		}
		return 0;
	}

	public static List<String> getGunLore(Gun g, ItemStack current, int amount) {
		List<String> lore = new ArrayList<>();
		if (g.getCustomLore() != null) {
			lore.addAll(g.getCustomLore());
		}
		// Charging
		ChargingHandler chargingHandler = g.getChargingVal();
		if (null != g.getChargingVal()) {
			String chargingLore = ChargingManager.getChargingLore(chargingHandler.getName(), "none");
			if (!"none".equals(chargingLore)) {
				lore.add(chargingLore);
			}
		}
		OLD_ItemFact.addVariantData(null, lore, g);
		lore.add(QAMain.S_ITEM_BULLETS + ": " + (amount) + "/" + (g.getMaxBullets()));
		if (QAMain.ENABLE_LORE_INFO) {
			lore.add(QAMain.S_ITEM_DAMAGE + ": " + g.getDamage());
			lore.add(QAMain.S_ITEM_DPS + ": "
					+ (g.isAutomatic()
					? (2 * g.getFireRate() * g.getDamage()) + ""
					+ (g.getBulletsPerShot() > 1 ? "x" + g.getBulletsPerShot() : "")
					: "" + ((int) (1.0 / g.getDelayBetweenShotsInSeconds()) * g.getDamage())
					+ (g.getBulletsPerShot() > 1 ? "x" + g.getBulletsPerShot() : "")));
			if (g.getAmmoType() != null)
				lore.add(QAMain.S_ITEM_AMMO + ": " + g.getAmmoType().getDisplayName());
		}
		if (QAMain.AutoDetectResourcepackVersion && Bukkit.getPluginManager().isPluginEnabled("ViaRewind")) {
			if (g.is18Support()) {
				lore.add(ChatColor.GRAY + "1.8 Weapon");
			}
		}

		if (QAMain.enableDurability)
			if (current == null) {
				lore.add(QAMain.S_ITEM_DURIB + ":" + g.getDurability() + "/" + g.getDurability());
			} else {
				lore = setDamage(g, lore, getDamage(current));
			}
		if (QAMain.ENABLE_LORE_HELP) {
			// Swapping RMB to LMB
			if (QAMain.SWAP_RMB_WITH_LMB) {
				// LMB to shoot, RMB to special
				lore.add(QAMain.S_SINGLE_LMB);
				// AutoFire mode
				if (g.isAutomatic()) {
					lore.add(QAMain.S_FULLAUTO_LMB);
				}
				// Aiming
				if (g.hasIronSights()) {
					if (QAMain.enableIronSightsON_RIGHT_CLICK) {
						lore.add(QAMain.S_Aiming_RMB);
					} else {
						lore.add(QAMain.S_Aiming_Shift);
					}
				}
				// reload
				if (QAMain.reloadOnFOnly) {
					lore.add(QAMain.S_RELOAD_F);
				} else {
					lore.add(QAMain.S_RELOAD_RMB);
				}
			} else {
				// RMB to shoot, LMB to special
				lore.add(QAMain.S_SINGLE_RMB);
				// AutoFire mode
				if (g.isAutomatic()) {
					lore.add(QAMain.S_FULLAUTO_RMB);
				}
				// Aiming
				if (g.hasIronSights()) {
					if (QAMain.enableIronSightsON_RIGHT_CLICK) {
						lore.add(QAMain.S_Aiming_LMB);
					} else {
						lore.add(QAMain.S_Aiming_Shift);
					}
				}
				// reload
				if (QAMain.reloadOnFOnly) {
					lore.add(QAMain.S_RELOAD_F);
				} else {
					lore.add(QAMain.S_RELOAD_LMB);
				}
			}
//			if (g.isAutomatic()) {
//				lore.add(QAMain.S_SINGLE_LMB);
//				lore.add(QAMain.S_FULLAUTO_LMB);
//				lore.add(QAMain.S_RELOAD_RMB);
//			} else {
//				lore.add(QAMain.S_SINGLE_LMB);
//				lore.add(QAMain.enableIronSightsON_RIGHT_CLICK ? QAMain.S_RELOAD_F : QAMain.S_RMB_R2);
//				if (g.hasIronSights())
//					lore.add(QAMain.enableIronSightsON_RIGHT_CLICK ? QAMain.S_Aiming_RMB : QAMain.S_Aiming_Shift);
//			}
		}

		if (current != null && current.hasItemMeta() && current.getItemMeta().hasLore())
			for (String s : current.getItemMeta().getLore()) {
				if (ChatColor.stripColor(s).contains("UUID")) {
					lore.add(s);
					break;
				}
			}
		return lore;
	}


	public static int getDamage(ItemStack is) {
		for (String lore : is.getItemMeta().getLore()) {
			if (ChatColor.stripColor(lore).startsWith(QAMain.S_ITEM_DURIB)) {
				return Integer.parseInt(lore.split(":")[1].split("/")[0].trim());
			}
		}
		return -1;
	}

	public static ItemStack damage(Gun g, ItemStack is) {
		return setDamage(g, is, getDamage(is) - 1);
	}

	public static ItemStack setDamage(Gun g, ItemStack is, int damage) {
		ItemMeta im = is.getItemMeta();
		im.setLore(setDamage(g, im.getLore(), damage));
		is.setItemMeta(im);
		return is;
	}

	public static List<String> setDamage(Gun g, List<String> lore, int damage) {
		boolean foundLine = false;
		double k = ((double) damage) / g.getDurability();
		ChatColor c = k > 0.5 ? ChatColor.DARK_GREEN : k > 0.25 ? ChatColor.GOLD : ChatColor.DARK_RED;
		for (int j = 0; j < lore.size(); j++) {
			if (ChatColor.stripColor(lore.get(j)).contains(QAMain.S_ITEM_DURIB)) {
				lore.set(j, c + QAMain.S_ITEM_DURIB + ":" + damage + "/" + g.getDurability());
				foundLine = true;
				break;
			}
		}
		if (!foundLine) {
			lore.add(c + QAMain.S_ITEM_DURIB + ":" + damage + "/" + g.getDurability());
		}
		return lore;
	}

	private static final String CALCTEXT = ChatColor.BLACK + "useddata:";

	public static int getCalculatedExtraDurib(ItemStack is) {
		if (!is.hasItemMeta() || !is.getItemMeta().hasLore() || is.getItemMeta().getLore().isEmpty())
			return -1;
		List<String> lore = is.getItemMeta().getLore();
		for (int i = 0; i < lore.size(); i++) {
			if (lore.get(i).startsWith(CALCTEXT))
				return Integer.parseInt(lore.get(i).split(CALCTEXT)[1]);
		}
		return -1;
	}

	public static ItemStack addCalulatedExtraDurib(ItemStack is, int number) {
		ItemMeta im = is.getItemMeta();
		List<String> lore = im.getLore();
		if (lore == null) {
			lore = new ArrayList<>();
		} else {
			if (getCalculatedExtraDurib(is) != -1)
				is = removeCalculatedExtra(is);
		}
		lore.add(CALCTEXT + number);
		im.setLore(lore);
		is.setItemMeta(im);
		return is;
	}

	public static ItemStack decrementCalculatedExtra(ItemStack is) {
		ItemMeta im = is.getItemMeta();
		List<String> lore = is.getItemMeta().getLore();
		for (int i = 0; i < lore.size(); i++) {
			if (lore.get(i).startsWith(CALCTEXT)) {
				lore.set(i, CALCTEXT + "" + (Integer.parseInt(lore.get(i).split(CALCTEXT)[1]) - 1));
			}
		}
		im.setLore(lore);
		is.setItemMeta(im);
		return is;
	}
	public static ItemStack removeCalculatedExtra(ItemStack is) {
		if (is.hasItemMeta() && is.getItemMeta().hasLore()) {
			ItemMeta im = is.getItemMeta();
			List<String> lore = is.getItemMeta().getLore();
			for (int i = 0; i < lore.size(); i++) {
				if (lore.get(i).startsWith(CALCTEXT)) {
					lore.remove(i);
				}
			}
			im.setLore(lore);
			is.setItemMeta(im);
		}
		return is;
	}


}
