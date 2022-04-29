package me.wither.ogminions

import com.massivecraft.massivecore.MassivePlugin
import me.wither.ogminions.colls.ConfigColl
import me.wither.ogminions.colls.MPlayerColl
import me.wither.ogminions.colls.MinionColl
import me.wither.ogminions.colls.getConf
import me.wither.ogminions.commands.CmdMinions
import me.wither.ogminions.entities.handlers.*
import me.wither.ogminions.events.Events
import me.wither.ogminions.events.MinionEngine
import me.wither.ogminions.extra.EngineGui
import me.wither.ogminions.extra.getAllOnlineMinions
import me.wither.ogminions.integrations.IntegrationHoloDisplay
import me.wither.ogminions.integrations.IntegrationSuperiorSkyblock
import me.wither.ogminions.integrations.IntegrationWildStacker
import me.wither.ogminions.tasks.*
import net.milkbowl.vault.permission.Permission
import org.bukkit.Bukkit
import org.bukkit.plugin.RegisteredServiceProvider

class OGMinionsPlugin: MassivePlugin() {
    init {
        inst = this
    }

    companion object {
        private lateinit var inst: OGMinionsPlugin
        @JvmStatic
        fun get(): OGMinionsPlugin { return inst }
    }

    override fun onEnableInner() {
        activate(
            MPlayerColl::class.java,
            ConfigColl::class.java,
            MinionColl::class.java,

            IntegrationSuperiorSkyblock::class.java,
            IntegrationWildStacker::class.java,
            IntegrationHoloDisplay::class.java,

            EngineGui::class.java,
            Events::class.java,
            MinionEngine::class.java,

            CmdMinions::class.java
        )
    }

    override fun onDisable() {
        Bukkit.getScheduler().cancelTasks(this)
        getAllOnlineMinions().forEach { it.despawn() }
        this.onDisable()
    }

    override fun onEnablePost() {
        MinionEngine.get().register(MinionColl.get().getMiner(), MinerHandler())
        MinionEngine.get().register(MinionColl.get().getButcher(), ButcherHandler())
        MinionEngine.get().register(MinionColl.get().getFeeder(), FeederHandler())
        MinionEngine.get().register(MinionColl.get().getHunter(), HunterHandler())
        MinionEngine.get().register(MinionColl.get().getReaper(), ReaperHandler())
        setupPermissions()
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, MinerActionTask(), 200L, MinionEngine.get().getConf("miner")!!.actionSpeedTicks)
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, FeederActionTask(), 200L, MinionEngine.get().getConf("feeder")!!.actionSpeedTicks)
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, ReaperActionTask(), 200L, MinionEngine.get().getConf("reaper")!!.actionSpeedTicks)
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, HunterActionTask(), 200L, MinionEngine.get().getConf("hunter")!!.actionSpeedTicks)
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, ButcherActionTask(), 200L, MinionEngine.get().getConf("butcher")!!.actionSpeedTicks)
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, StartAnimation.get(), 25L, 2L)
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, HoloUpdateTask(), getConf().minionNameTagUpdateTick, getConf().minionNameTagUpdateTick)
        super.onEnablePost()
    }

    lateinit var perms: Permission

    private fun setupPermissions(): Boolean {
        val rsp: RegisteredServiceProvider<Permission> = this.server.servicesManager.getRegistration(
            Permission::class.java)
        perms = rsp.provider
        return true
    }
}