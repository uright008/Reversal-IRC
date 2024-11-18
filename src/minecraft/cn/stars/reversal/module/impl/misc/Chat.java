package cn.stars.reversal.module.impl.misc;

import cn.stars.reversal.module.Category;
import cn.stars.reversal.module.Module;
import cn.stars.reversal.module.ModuleInfo;
import cn.stars.reversal.value.impl.BoolValue;

@ModuleInfo(name = "Chat", chineseName = "聊天", description = "Edit chat options", chineseDescription = "修改聊天选项", category = Category.MISC)
public class Chat extends Module {
    public final BoolValue chatBackground = new BoolValue("Chat Background", this, true);
    public final BoolValue combineRepeatedMsg = new BoolValue("Combine Repeated Messages", this, false);
}
