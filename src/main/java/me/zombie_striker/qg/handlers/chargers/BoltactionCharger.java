package me.zombie_striker.qg.handlers.chargers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.zombie_striker.qg.QAMain;
import me.zombie_striker.qg.guns.Gun;
import me.zombie_striker.qg.guns.utils.WeaponSounds;

public class BoltactionCharger implements ChargingHandler {

	List<UUID> timeC = new ArrayList<>();
	List<UUID> timeR = new ArrayList<>();
	
	public BoltactionCharger() {
		ChargingManager.add(this);
	}

	@Override
	public String getName() {
		return ChargingManager.BOLT;
	}

	@Override
	public boolean isCharging(Player player) {
		return timeC.contains(player.getUniqueId());
	}


	@Override
	public boolean isReadyToFire(Gun g, final Player player, ItemStack stack) {
		timeR.add(player.getUniqueId());
		new BukkitRunnable() {
			@Override
			public void run() {
				try {
					if (QAMain.isVersionHigherThan(1, 9)) {
						player.getWorld().playSound(player.getLocation(), WeaponSounds.RELOAD_BOLT.getSoundName(), 1, 1f);
					} else
						player.getWorld().playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, 5, 1);
				} catch (Error e) {
					try {
						player.getWorld().playSound(player.getLocation(), Sound.valueOf("CLICK"), 5, 1);
					} catch (Error | Exception e43) {
					}
				}
			}
		}.runTaskLater(QAMain.getInstance(), 10);
		new BukkitRunnable() {
			@Override
			public void run() {
				try {
					if (QAMain.isVersionHigherThan(1, 9)) {
						player.getWorld().playSound(player.getLocation(), WeaponSounds.RELOAD_BOLT.getSoundName(), 1, 1f);
					} else
						player.getWorld().playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, 5, 1);
				} catch (Error e) {
					try {
						player.getWorld().playSound(player.getLocation(), Sound.valueOf("CLICK"), 5, 1);
					} catch (Error | Exception e43) {
					}
				}
				timeR.remove(player.getUniqueId());
			}
		}.runTaskLater(QAMain.getInstance(), 16);
		return true;
	}

	@Override
	public boolean useChargingShoot() {
		return false;
	}

	@Override
	public void shoot(Gun g, Player player, ItemStack stack) {

	}

}
