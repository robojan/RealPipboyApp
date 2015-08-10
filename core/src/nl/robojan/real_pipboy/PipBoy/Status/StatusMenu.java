package nl.robojan.real_pipboy.PipBoy.Status;

import com.badlogic.gdx.math.Rectangle;

import nl.robojan.real_pipboy.Context;
import nl.robojan.real_pipboy.FalloutData.IFalloutData;
import nl.robojan.real_pipboy.FalloutData.GameString;
import nl.robojan.real_pipboy.FalloutData.StatusItem;
import nl.robojan.real_pipboy.FalloutData.StatusList;
import nl.robojan.real_pipboy.Constants;
import nl.robojan.real_pipboy.PipBoy.Controls.CardInfo;
import nl.robojan.real_pipboy.PipBoy.Controls.Control;
import nl.robojan.real_pipboy.PipBoy.Controls.Line;
import nl.robojan.real_pipboy.PipBoy.Controls.ListBox;
import nl.robojan.real_pipboy.PipBoy.Controls.SuperTextBox;
import nl.robojan.real_pipboy.PipBoy.Controls.Text;
import nl.robojan.real_pipboy.PipBoy.Controls.VerticalFadeLine;
import nl.robojan.real_pipboy.util.GameFileResolver;

/**
 * Created by s120330 on 6-7-2015.
 */
public class StatusMenu extends Control {


    public enum Page {
        STATUS, SPECIAL, SKILLS, PERKS, GENERAL
    }

    private Page mSelectedPage = Page.STATUS;

    // Header
    Line stats_headline_h1;
    VerticalFadeLine stats_headline_v1;
    Line stats_headline_h2;
    Text stats_title;
    CardInfo stats_lvl_info;
    CardInfo stats_hp_info;
    CardInfo stats_ap_info;
    CardInfo stats_xp_info;

    // footer
    SuperTextBox stats_tailline_status;
    SuperTextBox stats_tailline_SPECIAL;
    SuperTextBox stats_tailline_skills;
    SuperTextBox stats_tailline_perks;
    SuperTextBox stats_tailline_general;
    VerticalFadeLine stats_tailline_vleft;
    Line stats_tailline_h1;
    Line stats_tailline_h2;
    Line stats_tailline_h3;
    Line stats_tailline_h4;
    Line stats_tailline_h5;
    Line stats_tailline_h6;
    VerticalFadeLine stats_tailline_vright;

    // Sub Pages
    Status stats_status_container;

    // Info section
    private StatisticsDisplay stats_display_rect;
    private ListBox stats_special_container;
    private StatusList mSpecialItems;
    private ListBox stats_skills_container;
    private StatusList mSkillsItems;
    private ListBox stats_perks_container;
    private StatusList mPerksItems;

    private General stats_genrep_container;


    public StatusMenu() {
        super(0, 0);

        createHeader(0, 0);
        createFooter(50, 652, 855, 75);

        // Pages
        float height = stats_tailline_vleft.getTop() - stats_headline_v1.getBottom();
        // Status
        stats_status_container = new Status(50, 110, 855,
                stats_tailline_vleft.getTop()-110);
        addChild(stats_status_container);
        // SPECIAL
        stats_special_container = new ListBox(stats_headline_h1.getX() -
                (32-stats_headline_v1.getWidth())/2, stats_headline_v1.getBottom(), 325, height,
                false,20);
        stats_special_container.setNumberOfVisibleItems(9);
        mSpecialItems = new StatusList();
        // Skills
        stats_skills_container = new ListBox(stats_headline_h1.getX() -
                (32-stats_headline_v1.getWidth())/2, stats_headline_v1.getBottom(), 350, height,
                false,20);
        stats_skills_container.setNumberOfVisibleItems(9);
        mSkillsItems = new StatusList();
        // Perks
        stats_perks_container = new ListBox(stats_headline_h1.getX() -
                (32-stats_headline_v1.getWidth())/2, stats_headline_v1.getBottom(), 420, height,
                false,20);
        stats_perks_container.setNumberOfVisibleItems(9);
        mPerksItems = new StatusList();
        // General
        stats_genrep_container = new General(20, 50, 855,
                stats_tailline_vleft.getTop() - 50);

        // Common
        stats_display_rect = new StatisticsDisplay(0,stats_headline_v1.getBottom(),
                905, height);
        addChild(stats_display_rect);

        setPage(Page.STATUS);
    }

    private void createHeader(float x, float y) {
        stats_headline_h1 = new Line(x + 50, y + 50, 50);
        stats_headline_v1 = new VerticalFadeLine(x + 50, y + 50);
        stats_title = new Text(x + 120, y + 50, GameString.getString("sStats"), 4);
        stats_headline_h2 = new Line(x + 250, y + 50, 60);
        stats_lvl_info = new CardInfo( x + 270, y + 50, 100, 60,
                GameString.getString("sStatsLVLAbbrev"), "*");
        stats_hp_info = new CardInfo( x + 380, y + 50, 155, 60,
                GameString.getString("sStatsHP"), "*/*");
        stats_ap_info = new CardInfo( x + 545, y + 50, 135, 60,
                GameString.getString("sStatsAP"), "*/*");
        stats_xp_info = new CardInfo( x + 690, y + 50, 205, 60,
                GameString.getString("sStatsXP"), "*/*");
        addChild(stats_headline_h1);
        addChild(stats_headline_v1);
        addChild(stats_title);
        addChild(stats_headline_h2);
        addChild(stats_lvl_info);
        addChild(stats_hp_info);
        addChild(stats_ap_info);
        addChild(stats_xp_info);
    }

    private void createFooter(float x, float y, float width, float stretch) {
        float buttonCenter = (width+stretch*2)/6;

        // Create listener
        Control.ClickableListener pageListener = new ClickableListener() {
            @Override
            public void onClickableEvent(Control source, Object user, boolean secondary) {
                if(source == stats_tailline_status)
                {
                    setPage(Page.STATUS);
                } else if (source == stats_tailline_SPECIAL)
                {
                    setPage(Page.SPECIAL);
                } else if (source == stats_tailline_skills)
                {
                    setPage(Page.SKILLS);
                } else if (source == stats_tailline_perks)
                {
                    setPage(Page.PERKS);
                } else if (source == stats_tailline_general)
                {
                    setPage(Page.GENERAL);
                }
            }
        };

        // Create buttons
        stats_tailline_status = new SuperTextBox(0,0,GameString.getString("sStatsStatus"), true);
        stats_tailline_SPECIAL = new SuperTextBox(0,0,GameString.getString("sStatsSpecial"));
        stats_tailline_skills = new SuperTextBox(0,0,GameString.getString("sStatsSkills"));
        stats_tailline_perks = new SuperTextBox(0,0,GameString.getString("sStatsPerks"));
        stats_tailline_general = new SuperTextBox(0,0,GameString.getString("sStatsGeneral"));
        stats_tailline_status.addClickableListener(pageListener);
        stats_tailline_SPECIAL.addClickableListener(pageListener);
        stats_tailline_skills.addClickableListener(pageListener);
        stats_tailline_perks.addClickableListener(pageListener);
        stats_tailline_general.addClickableListener(pageListener);
        stats_tailline_status.setClickSound("sound/fx/ui/menu/ui_menu_ok.wav");
        stats_tailline_SPECIAL.setClickSound("sound/fx/ui/menu/ui_menu_ok.wav");
        stats_tailline_skills.setClickSound("sound/fx/ui/menu/ui_menu_ok.wav");
        stats_tailline_perks.setClickSound("sound/fx/ui/menu/ui_menu_ok.wav");
        stats_tailline_general.setClickSound("sound/fx/ui/menu/ui_menu_ok.wav");

        // Set positions
        // Status
        stats_tailline_status.setX(x + buttonCenter * 1 - stretch -
                stats_tailline_status.getSize().getWidth() / 2);
        stats_tailline_status.setY(y - stats_tailline_status.getSize().getHeight() / 2);
        // SPECIAL
        stats_tailline_SPECIAL.setX(x + buttonCenter * 2 - stretch -
                stats_tailline_SPECIAL.getSize().getWidth() / 2);
        stats_tailline_SPECIAL.setY(y - stats_tailline_SPECIAL.getSize().getHeight() / 2);
        // Skills
        stats_tailline_skills.setX(x + buttonCenter * 3 - stretch -
                stats_tailline_skills.getSize().getWidth() / 2);
        stats_tailline_skills.setY(y - stats_tailline_skills.getSize().getHeight() / 2);
        // Perks
        stats_tailline_perks.setX(x + buttonCenter * 4 - stretch -
                stats_tailline_perks.getSize().getWidth() / 2);
        stats_tailline_perks.setY(y - stats_tailline_perks.getSize().getHeight() / 2);
        // General
        stats_tailline_general.setX(x + buttonCenter * 5 - stretch -
                stats_tailline_general.getSize().getWidth() / 2);
        stats_tailline_general.setY(y - stats_tailline_general.getSize().getHeight() / 2);

        // Add connecting lines
        stats_tailline_vleft = new VerticalFadeLine(x, y, true, 60);
        stats_tailline_h1 = new Line(x, y, stats_tailline_status.getX() - x);
        stats_tailline_h2 = new Line((stats_tailline_status.getX() +
                stats_tailline_status.getSize().getWidth()), y, stats_tailline_SPECIAL.getX() -
                (stats_tailline_status.getX() + stats_tailline_status.getSize().getWidth()));
        stats_tailline_h3 = new Line((stats_tailline_SPECIAL.getX() +
                stats_tailline_SPECIAL.getSize().getWidth()), y, stats_tailline_skills.getX() -
                (stats_tailline_SPECIAL.getX() + stats_tailline_SPECIAL.getSize().getWidth()));
        stats_tailline_h4 = new Line((stats_tailline_skills.getX() +
                stats_tailline_skills.getSize().getWidth()), y, stats_tailline_perks.getX() -
                (stats_tailline_skills.getX() + stats_tailline_skills.getSize().getWidth()));
        stats_tailline_h5 = new Line((stats_tailline_perks.getX() +
                stats_tailline_perks.getSize().getWidth()), y, stats_tailline_general.getX() -
                (stats_tailline_perks.getX() + stats_tailline_perks.getSize().getWidth()));
        stats_tailline_h6 = new Line((stats_tailline_general.getX() +
                stats_tailline_general.getSize().getWidth()), y, x + width -
                (stats_tailline_general.getX() + stats_tailline_general.getSize().getWidth()));
        stats_tailline_vright = new VerticalFadeLine(x + width - Constants.LINE_THICKNESS, y, true,
                60);

        // Add childs
        addChild(stats_tailline_status);
        addChild(stats_tailline_SPECIAL);
        addChild(stats_tailline_skills);
        addChild(stats_tailline_perks);
        addChild(stats_tailline_general);
        addChild(stats_tailline_vleft);
        addChild(stats_tailline_h1);
        addChild(stats_tailline_h2);
        addChild(stats_tailline_h3);
        addChild(stats_tailline_h4);
        addChild(stats_tailline_h5);
        addChild(stats_tailline_h6);
        addChild(stats_tailline_vright);
    }

    public void setPage(Page page) {
        mSelectedPage = page;

        stats_status_container.setVisible(page == Page.STATUS);
        stats_tailline_status.setSelected(mSelectedPage == Page.STATUS);
        stats_tailline_SPECIAL.setSelected(mSelectedPage == Page.SPECIAL);
        stats_tailline_skills.setSelected(mSelectedPage == Page.SKILLS);
        stats_tailline_perks.setSelected(mSelectedPage == Page.PERKS);
        stats_tailline_general.setSelected(mSelectedPage == Page.GENERAL);
        int selected;
        switch(page){
            case STATUS:
                break;
            case SPECIAL:
                stats_display_rect.setX(mSpecialItems.hasEffect ? 350 : 325);
                selected = stats_special_container.getSelectedIndex();
                if(selected > 0 && selected < mSpecialItems.items.size){
                    setSelectedSPECIALItem(selected);
                } else {
                    setSelectedSPECIALItem(-1);
                }
                break;
            case SKILLS:
                stats_display_rect.setX(mSkillsItems.hasEffect ? 400 : 350);
                selected = stats_skills_container.getSelectedIndex();
                if(selected > 0 && selected < mSkillsItems.items.size){
                    setSelectedSkillItem(selected);
                } else {
                    setSelectedSkillItem(-1);
                }
                break;
            case PERKS:
                stats_display_rect.setX(420);
                selected = stats_perks_container.getSelectedIndex();
                if(selected > 0 && selected < mPerksItems.items.size){
                    setSelectedPerkItem(selected);
                } else {
                    setSelectedPerkItem(-1);
                }
                break;
            case GENERAL:
                break;
        }

        if(page ==Page.SPECIAL) {
            if(!containsChild(stats_special_container))
                addChild(stats_special_container, true);
        } else {
            removeChild(stats_special_container);
        }

        if(page ==Page.SKILLS) {
            if(!containsChild(stats_skills_container))
                addChild(stats_skills_container, true);
        } else {
            removeChild(stats_skills_container);
        }

        if(page ==Page.PERKS) {
            if(!containsChild(stats_perks_container))
                addChild(stats_perks_container, true);
        } else {
            removeChild(stats_perks_container);
        }

        if(page ==Page.GENERAL) {
            if(!containsChild(stats_genrep_container))
                addChild(stats_genrep_container, true);
        } else {
            removeChild(stats_genrep_container);
        }

        if(page != Page.STATUS && page != Page.GENERAL) {
            if(!containsChild(stats_display_rect))
                addChild(stats_display_rect, true);
        } else {
            removeChild(stats_display_rect);
        }
    }

    @Override
    public Rectangle getSize() {
        return new Rectangle(0, 0, Constants.PIPBOY_WIDTH, Constants.PIPBOY_HEIGHT);
    }

    @Override
    public void update(Context context) {
        IFalloutData data = context.foData;
        super.update(context);
        stats_lvl_info.setValue(Integer.toString(data.getLevel()));
        StringBuilder sb = new StringBuilder();
        sb.append(data.getHP());
        sb.append('/');
        sb.append(data.getMaxHP());
        stats_hp_info.setValue(sb.toString());
        sb.setLength(0);
        sb.append(data.getAP());
        sb.append('/');
        sb.append(data.getMaxAP());
        stats_ap_info.setValue(sb.toString());
        sb.setLength(0);
        sb.append(data.getXP());
        sb.append('/');
        sb.append(data.getNextLevelXP());
        stats_xp_info.setValue(sb.toString());
        switch(mSelectedPage) {
            case SPECIAL:
                updateSPECIAL(data);
                break;
            case SKILLS:
                updateSkills(data);
                break;
            case PERKS:
                updatePerks(data);
                break;
        }
    }

    private void updateSPECIAL(IFalloutData data) {
        StatusList list = data.getSPECIALList();
        if(!list.equivalent(mSpecialItems)){
            stats_special_container.clearItems();
            mSpecialItems = list;
            stats_display_rect.setX(list.hasEffect ? 350 : 325);
            stats_special_container.setWidth(list.hasEffect ? 350 : 325);
            for(int i = 0; i < list.items.size; i++) {
                StatusItem info = list.items.get(i);
                StatsItem item = new StatsItem(0,0, stats_special_container.getWidth(),
                        info.name, info.level, info.extra);
                item.setRightBuffer(list.hasEffect ? 20 + 35 : 20);
                item.addClickableListener(onSPECIALClick, i);
                item.setClickSound("sound/fx/ui/pipboy/ui_pipboy_scroll.wav");
                stats_special_container.addItem(item);
            }
        }
    }

    private void updateSkills(IFalloutData data) {
        StatusList list = data.getSkillsList();
        if(!list.equivalent(mSkillsItems)){
            stats_skills_container.clearItems();
            mSkillsItems = list;
            stats_display_rect.setX(list.hasEffect ? 400 : 350);
            stats_skills_container.setWidth(list.hasEffect ? 400 : 350);
            for(int i = 0; i < list.items.size; i++) {
                StatusItem info = list.items.get(i);
                StatsItem item = new StatsItem(0,0, stats_skills_container.getWidth(),
                        info.name, info.level, info.extra);
                item.setRightBuffer(list.hasEffect ? 20 + 35 : 20);
                item.addClickableListener(onSkillsClick, i);
                item.setClickSound("sound/fx/ui/pipboy/ui_pipboy_scroll.wav");
                stats_skills_container.addItem(item);
            }
        }
    }

    private void updatePerks(IFalloutData data) {
        StatusList list = data.getPerksList();
        if(!list.equivalent(mPerksItems)){
            stats_perks_container.clearItems();
            mPerksItems = list;
            stats_display_rect.setX(420);
            stats_perks_container.setWidth(420);
            for(int i = 0; i < list.items.size; i++) {
                StatusItem info = list.items.get(i);
                StatsItem item = new StatsItem(0,0, stats_perks_container.getWidth(),
                        info.name, info.level, info.extra);
                item.setRightBuffer(list.hasEffect ? 20 + 35 : 20);
                item.addClickableListener(onPerksClick, i);
                item.setClickSound("sound/fx/ui/pipboy/ui_pipboy_scroll.wav");
                stats_perks_container.addItem(item);
            }
        }
    }

    private void setSelectedSPECIALItem(int index) {
        if(index < 0) {
            stats_special_container.highLightItem(-1);
            stats_display_rect.setIcon(null);
            stats_display_rect.setBadge(null);
            stats_display_rect.setDescription(null);
        } else {
            StatusItem info = mSpecialItems.items.get(index);
            stats_special_container.highLightItem(index);
            stats_display_rect.setIcon(GameFileResolver.combineWithFallback(info.icon.toLowerCase(),
                    Constants.FNF_IMAGE));
            stats_display_rect.setBadge(GameFileResolver.combineWithFallback(info.badge,
                    Constants.FNF_IMAGE, true));
            stats_display_rect.setDescription(info.description);
        }
    }

    private void setSelectedSkillItem(int index) {
        if(index < 0) {
            stats_skills_container.highLightItem(-1);
            stats_display_rect.setIcon(null);
            stats_display_rect.setBadge(null);
            stats_display_rect.setDescription(null);
        } else {
            StatusItem info = mSkillsItems.items.get(index);
            stats_skills_container.highLightItem(index);
            stats_display_rect.setIcon(GameFileResolver.combineWithFallback(info.icon,
                    Constants.FNF_IMAGE));
            stats_display_rect.setBadge(GameFileResolver.combineWithFallback(info.badge,
                    Constants.FNF_IMAGE, true));
            stats_display_rect.setDescription(info.description);
        }
    }

    private void setSelectedPerkItem(int index) {
        if(index < 0) {
            stats_perks_container.highLightItem(-1);
            stats_display_rect.setIcon(null);
            stats_display_rect.setBadge(null);
            stats_display_rect.setDescription(null);
        } else {
            StatusItem info = mPerksItems.items.get(index);
            stats_perks_container.highLightItem(index);
            stats_display_rect.setIcon(GameFileResolver.combineWithFallback(info.icon,
                    Constants.FNF_IMAGE));
            stats_display_rect.setBadge(GameFileResolver.combineWithFallback(info.badge,
                    Constants.FNF_IMAGE, true));
            stats_display_rect.setDescription(info.description);
        }
    }

    private Control.ClickableListener onSPECIALClick = new ClickableListener() {
        @Override
        public void onClickableEvent(Control source, Object user, boolean secondary) {
            if(user.getClass() != Integer.class || source.getClass() != StatsItem.class)
                return;
            int index = ((Integer)user);
            setSelectedSPECIALItem(index);
        }
    };

    private Control.ClickableListener onSkillsClick = new ClickableListener() {
        @Override
        public void onClickableEvent(Control source, Object user, boolean secondary) {
            if(user.getClass() != Integer.class || source.getClass() != StatsItem.class)
                return;
            int index = ((Integer)user);
            setSelectedSkillItem(index);
        }
    };

    private Control.ClickableListener onPerksClick = new ClickableListener() {
        @Override
        public void onClickableEvent(Control source, Object user, boolean secondary) {
            if(user.getClass() != Integer.class || source.getClass() != StatsItem.class)
                return;
            int index = ((Integer)user);
            setSelectedPerkItem(index);
        }
    };
}
