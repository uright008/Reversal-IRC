package cn.stars.starx.module.impl.hud;

import cn.stars.starx.StarX;
import cn.stars.starx.event.impl.Render2DEvent;
import cn.stars.starx.font.modern.FontManager;
import cn.stars.starx.module.Category;
import cn.stars.starx.module.Module;
import cn.stars.starx.module.ModuleInfo;
import cn.stars.starx.setting.impl.NoteValue;
import cn.stars.starx.util.render.ThemeType;
import cn.stars.starx.util.render.ThemeUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@ModuleInfo(name = "TestElement", description = "Only for test", category = Category.HUD)
public class TestElement extends Module {
    private final NoteValue note = new NoteValue("Only for test purpose. DO NOT enable this.", this);
    List<String> strings = new ArrayList<>();
    public TestElement() {
        setCanBeEdited(true);
        setX(100);
        setY(100);
        setWidth(500);
        setHeight(500);
        strings.add("唧唧复唧唧，木兰当户织。不闻机杼声，唯闻女叹息。");
        strings.add("问女何所思，问女何所忆。女亦无所思，女亦无所忆。昨夜见军帖，可汗大点兵，军书十二卷，卷卷有爷名。");
        strings.add("阿爷无大儿，木兰无长兄，愿为市鞍马，从此替爷征。");
        strings.add("东市买骏马，西市买鞍鞯，南市买辔头，北市买长鞭。");
        strings.add("旦辞爷娘去，暮宿黄河边，不闻爷娘唤女声，但闻黄河流水鸣溅溅。旦辞黄河去，暮至黑山头，不闻爷娘唤女声，但闻燕山胡骑鸣啾啾。");
        strings.add("万里赴戎机，关山度若飞。朔气传金柝，寒光照铁衣。将军百战死，壮士十年归。");
        strings.add("归来见天子，天子坐明堂。策勋十二转，赏赐百千强。可汗问所欲，木兰不用尚书郎，愿驰千里足，送儿还故乡。");
        strings.add("爷娘闻女来，出郭相扶将；阿姊闻妹来，当户理红妆；小弟闻姊来，磨刀霍霍向猪羊。");
        strings.add("开我东阁门，坐我西阁床，脱我战时袍，著我旧时裳。当窗理云鬓，对镜帖花黄。");
        strings.add("出门看火伴，火伴皆惊忙：同行十二年，不知木兰是女郎。");
        strings.add("雄兔脚扑朔，雌兔眼迷离；双兔傍地走，安能辨我是雄雌？");
        strings.add("①唧（jī ）唧复唧唧：一作“唧唧何力力”，又作“促织何唧唧”。唧唧，纺织机的声音。一说为叹息声，意思是木兰无心织布，停机叹息。");
        strings.add("②当（dāng）户：对着门或在门旁，泛指在家中。");
        strings.add("③机杼（zhù）声：织布机在织布时发出的声音。机，指织布机。杼，织布的梭子。");
        strings.add("④惟：只。一作“唯”。");
        strings.add("⑤忆：思念，惦记。");
        strings.add("⑥军帖（tiě）：征兵的文书。");
        strings.add("⑦可汗（kè hán）：古代北方少数民族对君主的称呼。大点兵：大规模征集兵士。");
        strings.add("⑧军书十二卷：征兵的名册很多卷。十二，表示很多，不是实数。");
        strings.add("⑨爷：父亲，当时北方呼父为“阿爷”。");
        strings.add("⑩为：为此（指代父从军）。市：买。鞍（ān）马：马匹和乘马用具。据《新唐书·兵志》记载，起自西魏的府兵制规定从军的人要自备武器、粮食和衣服。");
        strings.add("⑪鞯（jiān）：马鞍下的垫子。");
        strings.add("⑫辔（pèi）头：驾驭牲口用的嚼子、笼头和缰绳。");
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        int x = getX() + 2;
        int y = getY() + 2;
        FontManager.getRegular(24).drawString("--- Modern MFont Renderer ---", x, y, ThemeUtil.getThemeColorInt(ThemeType.LOGO));
        strings.forEach(i -> {
            FontManager.getRegular(20).drawString(i, x, y + 15 + 15 * strings.indexOf(i), new Color(255,255,255,250).getRGB());
        });
    }
}
