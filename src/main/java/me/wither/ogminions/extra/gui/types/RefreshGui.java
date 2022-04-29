package me.wither.ogminions.extra.gui.types;

import com.massivecraft.massivecore.MassiveCore;
import me.wither.ogminions.extra.gui.BaseGui;
import me.wither.ogminions.extra.gui.configuration.GuiDesign;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public abstract class RefreshGui extends BaseGui {

    private int refreshRunnableId = -1;

    public RefreshGui(Player player, GuiDesign guiDesign) {
        super(player, guiDesign);

        addGuiCloseTask(event -> stopRefresh());
    }

    public abstract long getRefreshTicks();

    public abstract void refresh();

    @Override
    protected void loadGui() {
        long refreshTicks = getRefreshTicks();

        if(refreshTicks > 0) {
            this.refreshRunnableId = Bukkit.getScheduler().runTaskTimer(MassiveCore.get(), this::refresh, refreshTicks, refreshTicks).getTaskId();
        }

        super.loadGui();
    }

    public void stopRefresh() {
        if(this.refreshRunnableId != -1) {
            Bukkit.getServer().getScheduler().cancelTask(this.refreshRunnableId);
            this.refreshRunnableId = -1;
        }
    }
}
