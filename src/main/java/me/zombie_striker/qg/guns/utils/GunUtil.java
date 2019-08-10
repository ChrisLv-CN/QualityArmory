package me.zombie_striker.qg.guns.utils;

import me.zombie_striker.qg.QAMain;
import me.zombie_striker.qg.ammo.Ammo;
import me.zombie_striker.qg.api.QAHeadShotEvent;
import me.zombie_striker.qg.api.QAWeaponDamageEntityEvent;
import me.zombie_striker.qg.api.QualityArmory;
import me.zombie_striker.qg.armor.BulletProtectionUtil;
import me.zombie_striker.qg.boundingbox.AbstractBoundingBox;
import me.zombie_striker.qg.boundingbox.BoundingBoxManager;
import me.zombie_striker.qg.guns.Gun;
import me.zombie_striker.qg.handlers.*;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import ru.beykerykt.lightapi.LightAPI;
import ru.beykerykt.lightapi.chunks.ChunkInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class GunUtil {

	protected static HashMap<UUID, Location> AF_locs = new HashMap<>();
	protected static HashMap<UUID, BukkitTask> AF_tasks = new HashMap<>();

	public static HashMap<UUID, BukkitTask> rapidfireshooters = new HashMap<>();

	public static void shootHandler(Gun g, Player p) {
		double sway = g.getSway() * AimManager.getSway(g, p.getUniqueId());
		if (g.usesCustomProjctiles()) {
			for (int i = 0; i < g.getBulletsPerShot(); i++) {
				Vector go = p.getLocation().getDirection().normalize();
				go.add(new Vector((Math.random() * 2 * sway) - sway, (Math.random() * 2 * sway) - sway,
						(Math.random() * 2 * sway) - sway)).normalize();
				Vector two = go.clone();// .multiply();
				g.getCustomProjectile().spawn(g, p.getEyeLocation(), p, two);
			}
		} else {
			shootInstantVector(g, p, sway, g.getDamage(), g.getBulletsPerShot(), g.getMaxDistance());
		}
	}

	public static double getTargetedSolidMaxDistance(Vector v, Location start, double maxDistance) {
		Location test = start.clone();
		for (int i = 0; i < maxDistance; i++) {
			if (test.getBlock().getType() != Material.AIR) {
				if (isSolid(test.getBlock(), test))
					return start.distance(test);
			}
			test.add(v);
		}
		return maxDistance;
	}

	@SuppressWarnings("deprecation")
	public static void shootInstantVector(Gun g, Player p, double sway, double damage, int shots, int range) {
		for (int i = 0; i < shots; i++) {
			Location start = p.getEyeLocation().clone();
			Vector normalizedDirection = p.getLocation().getDirection().normalize();
			normalizedDirection.add(new Vector((Math.random() * 2 * sway) - sway, (Math.random() * 2 * sway) - sway,
					(Math.random() * 2 * sway) - sway));
			Vector step = normalizedDirection.clone().multiply(QAMain.bulletStep);

			// Simple values to make it easier on the search
			// boolean posX = go.getX() > 0;
			// boolean posZ = go.getZ() > 0;
			Entity hitTarget = null;

			boolean overrideocculde = false;

			boolean headShot = false;

			Location bulletHitLoc = null;

			int maxDistance = (int) getTargetedSolidMaxDistance(step, start, range);
			double dis2 = maxDistance;

			// double degreeVector = Math.atan2(normalizedDirection.getX(),
			// normalizedDirection.getZ());
			// if (degreeVector > Math.PI)
			// degreeVector = 2 * Math.PI - degreeVector;

			List<Location> blocksThatWillPLAYBreak = new ArrayList<>();
			List<Location> blocksThatWillBreak = new ArrayList<>();

			Location centerTest = start.clone().add(normalizedDirection.clone().multiply(maxDistance / 2));

			for (Entity e : centerTest.getWorld().getNearbyEntities(centerTest, maxDistance / 2, maxDistance / 2,
					maxDistance / 2)) {
				if (e instanceof Damageable) {
					if (QAMain.avoidTypes.contains(e.getType()))
						continue;

					if (e != p && e != p.getVehicle() && e != p.getPassenger()) {
						double dis = e.getLocation().distance(start);
						if (dis > dis2)
							continue;
						// double degreeEntity = Math.atan2(e.getLocation().getX() - start.getX(),
						// e.getLocation().getZ() - start.getZ());
						// if (degreeEntity > Math.PI)
						// degreeEntity = 2 * Math.PI - degreeEntity;
						// if (Math.max(degreeEntity, degreeVector)
						// - Math.min(degreeEntity, degreeVector) < (dis > 10 ? Math.PI / 7 : Math.PI /
						// 2)) {

						AbstractBoundingBox box = BoundingBoxManager.getBoundingBox(e);

						Location test = start.clone();
						// If the entity is close to the line of fire.
						if (e instanceof Player) {
							Player player = (Player) e;
							if (player.getGameMode() == GameMode.SPECTATOR) {
								continue;
							}
							if (QAMain.hasParties && (!QAMain.friendlyFire)) {
								try {
									if (com.alessiodp.parties.api.Parties.getApi().getPartyPlayer(e.getUniqueId())
											.getPartyName().equalsIgnoreCase(com.alessiodp.parties.api.Parties.getApi()
													.getPartyPlayer(p.getUniqueId()).getPartyName()))
										continue;
								} catch (Error | Exception e43) {

								}
							}
						}
						boolean hit = false;
						// Clear this to make sure
						for (int dist = 0; dist < dis / QAMain.bulletStep; dist++) {
							test.add(step);
							if (box.intersects(p, test, e)) {
								hit = true;
								break;
							}
						}
						if (hit) {
							bulletHitLoc = test;
							dis2 = dis;
							hitTarget = e;
							headShot = box.allowsHeadshots() ? box.isHeadShot(test, e) : false;
							if (headShot) {
								QAHeadShotEvent headshotevent = new QAHeadShotEvent(e, p, g);
								Bukkit.getPluginManager().callEvent(headshotevent);
								headShot = !headshotevent.isCanceled();
							}
							if (headShot) {
								QAMain.DEBUG("Headshot!");
								if (QAMain.headshotPling) {
									try {
										p.playSound(p.getLocation(), QAMain.headshot_sound, 2, 1);
										if (!QAMain.isVersionHigherThan(1, 9))
											try {
												p.playSound(p.getLocation(), Sound.valueOf("LAVA_POP"), 6, 1);
											} catch (Error | Exception h4) {
											}

									} catch (Error | Exception h4) {
										p.playSound(p.getLocation(), Sound.valueOf("LAVA_POP"), 1, 1);
									}
								}
							}
						}
						// }
					}
				}
			}
			if (hitTarget != null) {
				if (!(hitTarget instanceof Player) || QualityArmory.allowGunsInRegion(hitTarget.getLocation())) {

					boolean negateHeadshot = false;
					boolean bulletProtection = false;

					double damageMAX = damage * (bulletProtection ? 0.1 : 1)
							* ((headShot && !negateHeadshot) ? (QAMain.HeadshotOneHit ? 50*g.getHeadshotMultiplier() : g.getHeadshotMultiplier())
							: 1);

					if (hitTarget instanceof Player) {
						bulletProtection = BulletProtectionUtil.stoppedBullet(p, bulletHitLoc, normalizedDirection);
						if (headShot) {
							negateHeadshot = BulletProtectionUtil.negatesHeadshot(p);
						}
					}

					QAWeaponDamageEntityEvent shootevent = new QAWeaponDamageEntityEvent(p, g, hitTarget, headShot,
							damage, bulletProtection);
					Bukkit.getPluginManager().callEvent(shootevent);
					if (!shootevent.isCanceled()) {

						if (hitTarget instanceof Player) {
							Player player = (Player) hitTarget;
							if (QAMain.enableArmorIgnore) {
								try {
									// damage = damage * ( 1 - min( 20, max( defensePoints / 5, defensePoints -
									// damage / ( toughness / 4 + 2 ) ) ) / 25 )
									double defensePoints = 0;
									double toughness = 0;
									for (ItemStack is : new ItemStack[]{player.getInventory().getHelmet(),
											player.getInventory().getChestplate(), player.getInventory().getLeggings(),
											player.getInventory().getBoots()}) {
										if (is != null) {
											if (!is.getItemMeta().getAttributeModifiers(Attribute.GENERIC_ARMOR)
													.isEmpty())
												for (AttributeModifier a : is.getItemMeta()
														.getAttributeModifiers(Attribute.GENERIC_ARMOR))
													defensePoints += a.getAmount();
											for (AttributeModifier a : is.getItemMeta()
													.getAttributeModifiers(Attribute.GENERIC_ARMOR_TOUGHNESS))
												toughness += a.getAmount();
										}
									}
									damageMAX = damageMAX * (1 - Math.min(20, Math.max(defensePoints / 5,
											defensePoints - damageMAX / (toughness / 4 + 2))) / 25);
								} catch (Error | Exception e5) {

								}
							}

							if (!bulletProtection) {

								BulletWoundHandler.bulletHit((Player) hitTarget,
										g.getAmmoType() == null ? 1 : g.getAmmoType().getPiercingDamage());
							} else {
								hitTarget.sendMessage(QAMain.S_BULLETPROOFSTOPPEDBLEEDING);
							}
						}

						if (hitTarget instanceof LivingEntity)
							((LivingEntity) hitTarget).setNoDamageTicks(0);
						((Damageable) hitTarget).damage(damageMAX, p);
						QAMain.DEBUG("Damaging entity " + hitTarget.getName() + " ( "
								+ ((LivingEntity) hitTarget).getHealth() + "/"
								+ ((LivingEntity) hitTarget).getMaxHealth() + " :" + damageMAX + " DAM)");
					} else {
						QAMain.DEBUG("Damaging entity CANCELED " + hitTarget.getName() + " ( "
								+ ((LivingEntity) hitTarget).getHealth() + "/"
								+ ((LivingEntity) hitTarget).getMaxHealth() + " :" + damageMAX + " DAM)");
					}

				}
			} else {
				QAMain.DEBUG("No enities hit.");
			}
			double smokeDistance = 0;
			List<Player> nonheard = start.getWorld().getPlayers();
			nonheard.remove(p);
			if (g.useMuzzleSmoke())
				ParticleHandlers.spawnMuzzleSmoke(p, start.clone().add(step.clone().multiply(7)));
			double distSqrt = dis2;// Math.sqrt(dis2);
			for (double dist = 0; dist < distSqrt/* (dis2 / Main.bulletStep) */; dist += QAMain.bulletStep) {
				start.add(step);

				boolean solid = isSolid(start.getBlock(), start);
				if ((solid || isBreakable(start.getBlock(), start)) && !blocksThatWillPLAYBreak.contains(
						new Location(start.getWorld(), start.getBlockX(), start.getBlockY(), start.getBlockZ()))) {
					blocksThatWillPLAYBreak.add(
							new Location(start.getWorld(), start.getBlockX(), start.getBlockY(), start.getBlockZ()));
				}
				if (QAMain.destructableBlocks.contains(start.getBlock().getType())) {
					blocksThatWillBreak.add(start);
				}

				try {
					int control = 3;
					if (dist % control == 0) {
						List<Player> heard = new ArrayList<>();
						for (Player p2 : nonheard) {
							if (p2.getLocation().distance(start) < control * 2) {
								try {
									start.getWorld().playSound(start, Sound.BLOCK_DISPENSER_LAUNCH, 2, 3);
								} catch (Error e) {
									start.getWorld().playSound(start, Sound.valueOf("SHOOT_ARROW"), 2, 2);
								}
								heard.add(p2);
							}
						}
						for (Player p3 : heard) {
							nonheard.remove(p3);
						}
					}

				} catch (Error | Exception e53) {
					if (dist % 30 == 0) {
						try {
							start.getWorld().playSound(start, Sound.BLOCK_DISPENSER_LAUNCH, 2, 2);
							start.getWorld().playSound(start, Sound.BLOCK_FIRE_EXTINGUISH, 2, 2);
						} catch (Error e) {
							start.getWorld().playSound(start, Sound.valueOf("SHOOT_ARROW"), 2, 2);
							start.getWorld().playSound(start, Sound.valueOf("FIRE_IGNITE"), 2, 2);
						}
					}
				}
				if (overrideocculde || !isSolid(start.getBlock(), start)) {
					if (QAMain.enableBulletTrails)
						// Chris: how many step to show a particles
						if (smokeDistance >= QAMain.smokeSpacing * QAMain.bulletStep) {
							ParticleHandlers.spawnGunParticles(g, start);
							smokeDistance = 0;
						} else {
							smokeDistance += QAMain.bulletStep;
						}
				} else {
					// start.getWorld().spawnParticle(Particle.BLOCK_DUST,start,start.getBlock().getTypeId());
					start.getWorld().playEffect(start, Effect.STEP_SOUND, start.getBlock().getType());
					break;
				}
			}

			for (Location l : blocksThatWillBreak) {
				l.getBlock().breakNaturally();
			}

			// TODO: Do lights n stuff
			try {
				if (Bukkit.getPluginManager().getPlugin("LightAPI") != null) {
					final Location loc = p.getEyeLocation().clone();
					LightAPI.createLight(loc, g.getLightOnShoot(), false);
					for (ChunkInfo c : LightAPI.collectChunks(loc)) {
						LightAPI.updateChunk(c);
					}
					new BukkitRunnable() {

						@Override
						public void run() {
							LightAPI.deleteLight(loc, false);
							for (ChunkInfo c : LightAPI.collectChunks(loc)) {
								LightAPI.updateChunk(c);
							}
						}
					}.runTaskLater(QAMain.getInstance(), 3);
				}
			} catch (Error | Exception e5) {
			}

			// Breaking texture
			if (QAMain.blockBreakTexture)
				for (@SuppressWarnings("unused")
						Location l : blocksThatWillPLAYBreak) {
					start.getWorld().playSound(start, SoundHandler.getSoundWhenShot(start.getBlock()), 2, 1);
					try {/*
					 * for (Player p2 : l.getWorld().getPlayers()) {
					 * com.comphenix.protocol.events.PacketContainer packet = new
					 * com.comphenix.protocol.events.PacketContainer(
					 * com.comphenix.protocol.Packets.Server.BLOCK_BREAK_ANIMATION);
					 * packet.getIntegers().write(0, p2.getEntityId());
					 * packet.getBlockPositionModifier().write(1, new
					 * com.comphenix.protocol.wrappers.BlockPosition(l.getBlockX(), l.getBlockY(),
					 * l.getBlockZ())); packet.getBytes().write(2, (byte) 4);
					 * com.comphenix.protocol.ProtocolLibrary.getProtocolManager().sendServerPacket(
					 * p2, packet); }
					 */
					} catch (Error | Exception e4) {
					}
				}
		}
	}

	public static void basicShoot(boolean offhand, Gun g, Player player, double acc) {
		basicShoot(offhand, g, player, acc, 1);
	}

	@SuppressWarnings("deprecation")
	public static void basicShoot(boolean offhand, final Gun g, final Player player, final double acc, int times) {
		long showdelay = ((int) (g.getDelayBetweenShotsInSeconds() * 1000));
		QAMain.DEBUG("About to shoot!");

		if (g.getChargingVal() != null && (g.getChargingVal().isCharging(player)
				|| (g.getReloadingingVal() != null && g.getReloadingingVal().isReloading(player))))
			return;

		if (g.getLastShotForGun().containsKey(player.getUniqueId())
				&& (System.currentTimeMillis() - g.getLastShotForGun().get(player.getUniqueId()) <= showdelay)) {
			QAMain.DEBUG("Shooting canceled due to last shot being too soon.");
			return;
		}
		g.getLastShotForGun().put(player.getUniqueId(), System.currentTimeMillis());

		final ItemStack temp = offhand ? Update19OffhandChecker.getItemStackOFfhand(player)
				: player.getInventory().getItemInHand();
		ItemMeta im = temp.getItemMeta();

		if (rapidfireshooters.containsKey(player.getUniqueId())) {
			QAMain.DEBUG("Shooting canceled due to rapid fire being enabled.");
			return;
		}

		boolean regularshoot = true;
		if (g.getChargingVal() != null && (!g.getChargingVal().isCharging(player)
				&& (g.getReloadingingVal() == null || !g.getReloadingingVal().isReloading(player)))) {
			regularshoot = g.getChargingVal().shoot(g, player, temp);
			QAMain.DEBUG("Charging shoot debug: " + g.getName() + " = " + g.getChargingVal() == null ? "null"
					: g.getChargingVal().getName());
		}
		if (regularshoot) {
			GunUtil.shootHandler(g, player);
			playShoot(g, player);
			if (QAMain.enableRecoil)
				addRecoil(player, g);

		}
		if (g.isAutomatic()) {
			rapidfireshooters.put(player.getUniqueId(), new BukkitRunnable() {
				int slotUsed = player.getInventory().getHeldItemSlot();
				boolean offhand = QualityArmory.isIronSights(player.getItemInHand());

				@Override
				public void run() {
					if (offhand) {
						if (!QualityArmory.isIronSights(player.getItemInHand())) {
							cancel();
							rapidfireshooters.remove(player.getUniqueId());
							return;
						}
					}

					// final AttachmentBase attach = QualityArmory.getGunWithAttchments(temp);

					int amount = Gun.getAmount(temp);
					if (!player.isSneaking() || slotUsed != player.getInventory().getHeldItemSlot() || amount <= 0) {
						rapidfireshooters.remove(player.getUniqueId()).cancel();
						return;
					}

					boolean regularshoot = true;
					if (g.getChargingVal() != null && (!g.getChargingVal().isCharging(player)
							&& (g.getReloadingingVal() == null || !g.getReloadingingVal().isReloading(player)))) {
						regularshoot = g.getChargingVal().shoot(g, player, temp);
						QAMain.DEBUG(
								"Charging (rapidfire) shoot debug: " + g.getName() + " = " + g.getChargingVal() == null
										? "null"
										: g.getChargingVal().getName());
					}

					if (regularshoot) {
						GunUtil.shootHandler(g, player);
						playShoot(g, player);
						if (QAMain.enableRecoil)
							addRecoil(player, g);
						// TODO: recoil
					}

					// GunUtil.shoot(g, player, g.getSway() * AimManager.getSway(g,
					// player.getUniqueId()), g.getDamage(), 1,
					// g.getMaxDistance());
					// GunUtil.playShoot(g, attach, player);
					amount--;

					if (amount < 0)
						amount = 0;

					// if (QAMain.enableVisibleAmounts) {
					// temp.setAmount(amount > 64 ? 64 : amount == 0 ? 1 : amount);
					// }
					ItemMeta im = temp.getItemMeta();
					int slot;
					if (offhand) {
						slot = -1;
					} else {
						slot = player.getInventory().getHeldItemSlot();
					}
					im.setLore(Gun.getGunLore(g, temp, amount));
					temp.setItemMeta(im);
					if (slot == -1) {
						try {
							if (QualityArmory.isIronSights(player.getItemInHand())) {
								player.getInventory().setItemInOffHand(temp);
							} else {
								player.getInventory().setItemInHand(temp);
							}

						} catch (Error e) {
						}
					} else {
						player.getInventory().setItem(slot, temp);
					}
					QualityArmory.sendHotbarGunAmmoCount(player, g, temp, false);
				}
			}.runTaskTimer(QAMain.getInstance(), 10 / g.getFireRate(), 10 / g.getFireRate()));
		}

		int amount = Gun.getAmount(temp) - 1;

		if (amount < 0)
			amount = 0;

		// if (QAMain.enableVisibleAmounts) {
		// temp.setAmount(amount > 64 ? 64 : amount == 0 ? 1 : amount);
		// }
		int slot;
		if (offhand) {
			slot = -1;
		} else {
			slot = player.getInventory().getHeldItemSlot();
		}
		im.setLore(Gun.getGunLore(g, temp, amount));
		temp.setItemMeta(im);
		if (slot == -1) {
			try {
				player.getInventory().setItemInOffHand(temp);
			} catch (Error e) {
			}
		} else {
			player.getInventory().setItem(slot, temp);
		}
	}

	public static void playShoot(final Gun g, final Player player) {
		new BukkitRunnable() {
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				try {
					String soundname = null;
					if (g.getWeaponSounds().size() > 1) {
						soundname = g.getWeaponSounds()
								.get(ThreadLocalRandom.current().nextInt(g.getWeaponSounds().size()));
					} else {
						soundname = g.getWeaponSound();
					}
					player.getWorld().playSound(player.getLocation(), soundname, (float)g.getVolume(), 1);
					if (!QAMain.isVersionHigherThan(1, 9)) {
						try {
							player.getWorld().playSound(player.getLocation(), Sound.valueOf("CLICK"), 5, 1);
							player.getWorld().playSound(player.getLocation(), Sound.valueOf("WITHER_SHOOT"), 8, 2);
							player.getWorld().playSound(player.getLocation(), Sound.valueOf("EXPLODE"), 8, 2f);
						} catch (Error | Exception e3) {
							player.getWorld().playSound(player.getLocation(), Sound.valueOf("BLOCK_LEVER_CLICK"), 5, 1);
						}
					}
				} catch (Error e2) {
					player.getWorld().playSound(player.getLocation(), Sound.valueOf("CLICK"), 5, 1);
					player.getWorld().playSound(player.getLocation(), Sound.valueOf("WITHER_SHOOT"), 8, 2);
					player.getWorld().playSound(player.getLocation(), Sound.valueOf("EXPLODE"), 8, 2f);
				}

			}
		}.runTaskLater(QAMain.getInstance(), 1);
		// Simply delaying the sound by 1/20th of a second makes shooting so much more
		// immersive
	}

	public static boolean hasAmmo(Player player, Gun g) {
		return QualityArmory.getAmmoInInventory(player, g.getAmmoType()) > 0;
	}

	public static void basicReload(final Gun g, final Player player, boolean doNotRemoveAmmo) {
		basicReload(g, player, doNotRemoveAmmo, 1.5);
	}

	public static void basicReload(final Gun g, final Player player, boolean doNotRemoveAmmo, double seconds) {
		@SuppressWarnings("deprecation")

		final ItemStack temp = player.getInventory().getItemInHand();
		ItemMeta im = temp.getItemMeta();

		if (Gun.getAmount(temp) == g.getMaxBullets()) {
			return;
		}
		if (im == null || !im.hasDisplayName())
			return;
		if (im.getLore() != null && im.getDisplayName().contains(QAMain.S_RELOADING_MESSAGE)) {

		} else {
			try {
				player.getWorld().playSound(player.getLocation(), WeaponSounds.RELOAD_MAG_OUT.getSoundName(), 1, 1f);
			} catch (Error e2) {
				try {
					player.getWorld().playSound(player.getLocation(), Sound.valueOf("CLICK"), 5, 1);
				} catch (Error | Exception e3) {
					player.getWorld().playSound(player.getLocation(), Sound.valueOf("BLOCK_LEVER_CLICK"), 5, 1);
				}
			}
			final int slot = player.getInventory().getHeldItemSlot();

			Ammo ammo = g.getAmmoType();

			final int initialAmount = Gun.getAmount(temp);
			final int reloadAmount = doNotRemoveAmmo ? g.getMaxBullets()
					: Math.min(g.getMaxBullets(), initialAmount + QualityArmory.getAmmoInInventory(player, ammo));
			final int subtractAmount = reloadAmount - initialAmount;
			if (!doNotRemoveAmmo)
				QualityArmory.removeAmmoFromInventory(player, ammo, subtractAmount);

			if (g.getReloadingingVal() != null) {
				seconds = g.getReloadingingVal().reload(player, g, subtractAmount);
			}
			QAMain.toggleNightvision(player, g, false);

			im.setLore(Gun.getGunLore(g, temp, 0));
			im.setDisplayName(g.getDisplayName() + QAMain.S_RELOADING_MESSAGE);
			temp.setItemMeta(im);
			// if (QAMain.enableVisibleAmounts)
			// temp.setAmount(1);
			player.getInventory().setItem(slot, temp);

			new GunRefillerRunnable(player, temp, g, slot, reloadAmount, seconds);

		}

	}

	public static HashMap<UUID, Double> highRecoilCounter = new HashMap<>();

	public static void addRecoil(final Player player, final Gun g) {
		if (g.getRecoil() == 0)
			return;
		if (g.getFireRate() >= 4) {
			if (highRecoilCounter.containsKey(player.getUniqueId())) {
				highRecoilCounter.put(player.getUniqueId(),
						highRecoilCounter.get(player.getUniqueId()) + g.getRecoil());
			} else {
				highRecoilCounter.put(player.getUniqueId(), g.getRecoil());
				new BukkitRunnable() {
					@Override
					public void run() {
						if (QAMain.hasProtocolLib && QAMain.isVersionHigherThan(1, 13) && !QAMain.hasViaVersion) {
							addRecoilWithProtocolLib(player, g, true);
						} else
							addRecoilWithTeleport(player, g, true);
					}
				}.runTaskLater(QAMain.getInstance(), 3);
			}
		} else {
			if (QAMain.hasProtocolLib && QAMain.isVersionHigherThan(1, 13)) {
				addRecoilWithProtocolLib(player, g, false);
			} else
				addRecoilWithTeleport(player, g, false);
		}
	}

	private static void addRecoilWithProtocolLib(Player player, Gun g, boolean useHighRecoil) {
		Vector newDir = player.getLocation().getDirection();
		newDir.normalize()
				.setY(newDir.getY()
						+ ((useHighRecoil ? highRecoilCounter.get(player.getUniqueId()) : g.getRecoil()) / 30))
				.multiply(20);
		if (useHighRecoil)
			highRecoilCounter.remove(player.getUniqueId());
		ProtocolLibHandler.sendYawChange(player, newDir);
	}

	private static void addRecoilWithTeleport(Player player, Gun g, boolean useHighRecoil) {
		Location tempCur = (QAMain.recoilHelperMovedLocation.get(player.getUniqueId()));
		final Location current;
		if (tempCur == null) {
			current = player.getLocation();
		} else {
			current = tempCur;
		}
		Vector movementOffset = player.getVelocity().multiply(0.2);
		if (movementOffset.getY() > -0.1 && movementOffset.getY() < 0)
			movementOffset.setY(0);
		current.add(movementOffset);
		current.setPitch((float) (current.getPitch()
				- (useHighRecoil ? highRecoilCounter.get(player.getUniqueId()) : g.getRecoil())));
		if (useHighRecoil)
			highRecoilCounter.remove(player.getUniqueId());
		Vector temp = player.getVelocity();
		// player.getLocation().setDirection(vector);
		player.teleport(current);
		player.setVelocity(temp);
	}

	public static boolean isBreakable(Block b, Location l) {
		if (b.getType().name().contains("GLASS"))
			return true;
		return false;

	}

	@SuppressWarnings("deprecation")
	public static boolean isSolid(Block b, Location l) {
		return BlockCollisionUtil.isSolid(b,l);
	}
}
