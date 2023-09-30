package tk.estecka.invarpaint.mixin;

import java.util.List;
import java.util.Set;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import com.llamalad7.mixinextras.MixinExtrasBootstrap;

public class Config 
implements IMixinConfigPlugin
{
	public void	onLoad(String mixinPackage){
		MixinExtrasBootstrap.init();
	}

	public String getRefMapperConfig(){ return null; }
	public boolean shouldApplyMixin(String _1, String _2) { return true; }
	public void acceptTargets(Set<String> _1, Set<String> _2) {}
	public List<String> getMixins() { return null; }
	public void preApply(String _1, ClassNode _2, String _3, IMixinInfo _4){}
	public void postApply(String _1, ClassNode _2, String _3, IMixinInfo _4){}
}
