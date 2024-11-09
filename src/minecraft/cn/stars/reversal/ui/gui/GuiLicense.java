/*
 * Reversal Client - A PVP Client with hack visual.
 * Copyright 2024 Starlight, All rights reserved.
 */
package cn.stars.reversal.ui.gui;

import cn.stars.reversal.GameInstance;
import cn.stars.reversal.RainyAPI;
import cn.stars.reversal.ui.curiosity.CuriosityTextButton;
import cn.stars.reversal.util.render.RenderUtil;
import cn.stars.reversal.util.render.RoundedUtil;
import cn.stars.reversal.util.render.UIUtil;
import cn.stars.reversal.util.shader.RiseShaders;
import cn.stars.reversal.util.shader.base.ShaderRenderType;
import lombok.SneakyThrows;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;

import static cn.stars.reversal.GameInstance.*;
import static cn.stars.reversal.GameInstance.UI_BLOOM_RUNNABLES;

public class GuiLicense extends GuiScreen {
    private CuriosityTextButton acceptButton, rejectButton;
    private CuriosityTextButton[] buttons;
    String license = "《用户服务协议和隐私政策》（以下简称“协议”）及其条款，系您下载、安装及使用“Reversal Client”（简称：Reversal）软件\n（以下简称“本软件”）所订立的、描述您与本软件之间权利义务的协议。\n" +
            "在注册前务必认真阅读本协议的内容、充分理解各条款内容，如有异议，您可选择不进入本软件。一旦您确认本用户使用\n软件后，本协议即在您和本软件之间产生法律效力，意味着您完全同意并接受协议的全部条款。请您审慎阅读并选择接受或\n不接受协议（未成年人应在法定监护人陪同下阅读）。\n\n" +
            "[1] 隐私收集 \n本软件可能读取电脑的硬件信息、Minecraft启动器信息、环境信息。其余所有内容均不纳入收集范围内。\n\n" +
            "[2] 使用条例 \n(*1) 禁止伪造、反编译、传播本软件的源代码 \n(*2) 本软件完全免费，禁止使用本软件进行商用或倒卖 \n(*3) 禁止散布本软件的任何虚假信息、谣言 \n(*4) 禁止使用技术手段监控或调试本软件的运行 \n" +
            "(5) 用户充分了解并同意，本软件为用户提供个性化信息服务，用户须为自己注册账号下的行为负责，包括用户所导入、上载、\n传送的任何内容以及由此产生的任何后果，用户应对本软件中的内容自行加以判断，并承担因使用内容而引起的所有风险。\n本软件不对因用户行为而导致的损失承担责任。\n" +
            "(6) 用户须对在本软件上所传送信息的真实性、合法性、有效性等负责，与用户所传播的信息相关的任何法律责任由用户\n自行承担，与本软件无关。用户在本软件中记录的或通过本软件服务所传送的任何内容并不反映本软件的观点或政策，\n本软件对此不承担责任。\n" +
            "(7) 用户禁止以文字、语音、图片等任何形式刻意宣传其他竞争平台；也禁止以相同的方式宣传非本软件联系方式。\n" +
            "(8) 本软件保留因业务发展需要，单方面对其服务器所提供的全部或部分服务内容在任何时候不经任何通知的情况下\n变更、暂停、限制、终止或撤销服务的权利。\n" +
            "(*9) 用户使用本软件服务制作、上载、复制、发布、传播或者转载的内容应完全符合国家或当地法律法规，\n若用户实施违反本条所述使用规则及法律法规的行为，所有责任由用户承担。\n\n" +
            "[3] 法律责任与免责 \n(1) 本团队对本软件因任何原因造成的任何服务中断不负责任。本团队将尽力维护本软件。 \n(2) 因本软件依赖的平台(如前置下载、网络服务商）造成的任何服务中断不负责任。 \n(3) 因用户违反本协议或相关的服务条款的规定，导致或产生的任何第三方向本软件主张的索赔，由用户自行承担。 \n" +
            "(4) 用户在本软件平台发表的观点及立场，并不代表本软件的立场，用户应自行对发表内容负责。 \n(5)请用户自行妥善保管个人资料，将个人信息提供足够保障及备份，并采取适当的预防措施降低电脑病毒或其他恶意\n破坏性举动的风险。\n\n" +
            "[4] 知识产权 \n本软件的一切知识产权，以及与软件相关的所有信息内容，包括但不限于：文字表述及其组合\n、图标、图饰、图像、图表、色彩、界面设计、版面框架、有关数据、附加程序、印刷材料或电子文档等均归本软件所有，\n受著作权法和国际著作权条约以及其他知识产权法律法规的保护。\n\n" +
            "[5] 修改与解释权 \n根据互联网的发展和有关法律、法规及规范性文件的变化，或者因业务发展需要，本软件有权对\n本协议的条款作出修改或变更。一旦本协议的内容发生变动，您可在本团队提供的平台查阅最新版协议条款，该公布行为视为\n本软件已经通知用户修改内容，而不另行对用户进行个别通知。" +
            "在本软件修改协议条款后，如果您不接受修改后的条款，\n请立即停止使用本软件提供的服务，您继续使用本软件提供的服务将被视为已接受了修改后的协议。\n\n" +
            "Copyright © 2024 Starlight, All rights reserved.";

    @Override
    public void initGui() {
        acceptButton = new CuriosityTextButton(this.width / 2 - 100, this.height - 60, 200, 20, () -> {
            RainyAPI.isLicenseReviewed = true;
            mc.displayGuiScreen(new GuiPreInit());
        }, "同意", "", true, 1, 90, 5, 20);
        rejectButton = new CuriosityTextButton(this.width / 2 - 100, this.height - 35, 200, 20, () -> mc.shutdown(), "拒绝", "", true, 1, 90, 5, 20);
        buttons = new CuriosityTextButton[]{acceptButton, rejectButton};
    }

    @SneakyThrows
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        // blur
        RiseShaders.GAUSSIAN_BLUR_SHADER.update();
        RiseShaders.GAUSSIAN_BLUR_SHADER.run(ShaderRenderType.OVERLAY, partialTicks, NORMAL_BLUR_RUNNABLES);
        // bloom
        RiseShaders.POST_BLOOM_SHADER.update();
        RiseShaders.POST_BLOOM_SHADER.run(ShaderRenderType.OVERLAY, partialTicks, NORMAL_POST_BLOOM_RUNNABLES);

        GameInstance.clearRunnables();

        RoundedUtil.drawRound(width / 2f - 225, 10, 450, height - 15, 4, new Color(30, 30, 30, 160));
        GameInstance.NORMAL_BLUR_RUNNABLES.add(() -> RoundedUtil.drawRound(width / 2f - 225, 10, 450, height - 15, 4, Color.BLACK));

        for (CuriosityTextButton button : buttons) {
            button.draw(mouseX, mouseY, partialTicks);
        }

        RenderUtil.rect(width / 2f - 225, 30, 450, 0.5, new Color(220, 220, 220, 240));
        RenderUtil.rect(width / 2f - 225, 56, 450, 0.5, new Color(220, 220, 220, 240));

        GameInstance.regular24Bold.drawCenteredString("最终用户许可协议", width / 2f, 16, new Color(220, 220, 220, 240).getRGB());

        GameInstance.regular20Bold.drawString("在使用本客户端前，请详细阅读以下协议。拒绝协议无法进入游戏。", width / 2f - 220, 35, new Color(220, 220, 220, 240).getRGB());
        GameInstance.regular20Bold.drawString("对于任何协议的违反行为，本制作团队(Starlight)保留追责权利。", width / 2f - 220, 45, new Color(220, 220, 220, 240).getRGB());

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderUtil.scissor(width / 2f - 225, 57, 450, height - 15 - 57);
        GameInstance.regular16.drawString(license, width / 2f - 220, 67, new Color(220, 220, 220, 240).getRGB());
        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        UI_BLOOM_RUNNABLES.forEach(Runnable::run);
        UI_BLOOM_RUNNABLES.clear();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        UIUtil.onButtonClick(buttons, mouseX, mouseY, mouseButton);
    }
}
