package nl.robojan.real_pipboy.PipBoy.Items;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;

import nl.robojan.real_pipboy.Context;
import nl.robojan.real_pipboy.FalloutData.AidItem;
import nl.robojan.real_pipboy.FalloutData.AmmoItem;
import nl.robojan.real_pipboy.FalloutData.ApparelItem;
import nl.robojan.real_pipboy.FalloutData.IFalloutData;
import nl.robojan.real_pipboy.FalloutData.GameString;
import nl.robojan.real_pipboy.FalloutData.Item;
import nl.robojan.real_pipboy.FalloutData.ItemsList;
import nl.robojan.real_pipboy.FalloutData.MiscItem;
import nl.robojan.real_pipboy.FalloutData.WeaponItem;
import nl.robojan.real_pipboy.PipBoy.Constants;
import nl.robojan.real_pipboy.PipBoy.Controls.Control;
import nl.robojan.real_pipboy.PipBoy.Controls.Image;
import nl.robojan.real_pipboy.PipBoy.Controls.ListBox;
import nl.robojan.real_pipboy.PipBoy.Controls.TabLine;
import nl.robojan.real_pipboy.PipBoy.Controls.Text;
import nl.robojan.real_pipboy.PipBoy.Controls.TextBox;
import nl.robojan.real_pipboy.util.GameFileResolver;

/**
 * Created by s120330 on 12-7-2015.
 */
public class InventoryMenu extends Control {
    private float mWidth, mHeight;

    private MainRect IM_MainRect;
    private TabLine IM_Tabline;

    private final static String[] TABLINE_BUTTONS = new String[] { "Weapons", "Apparel", "Aid",
        "Misc", "ammo"};

    public InventoryMenu() {
        super(0, 0);
        mWidth = Constants.PIPBOY_WIDTH;
        mHeight = Constants.PIPBOY_HEIGHT;

        IM_MainRect = new MainRect(50, 50, 855, 650);
        addChild(IM_MainRect);

        IM_Tabline = new TabLine(50, 650, 855, TABLINE_BUTTONS);
        addChild(IM_Tabline);
    }

    @Override
    public Rectangle getSize() {
        return new Rectangle(mX, mY, mWidth, mHeight);
    }

    public class MainRect extends Control {
        private float mWidth, mHeight;

        private HeadlineRect IM_HeadlineRect;
        private ListBox IM_InventoryList;

        private TextBox IM_RepairButton;
        private TextBox IM_ModButton;

        private Image IM_ItemIcon;
        private Image IM_ItemIconBadge;

        private Text IM_StrReq;

        private ItemStatsDisplay IM_ItemInfoRect;

        private ItemsList mItems = null;
        private ItemsList[] mItemsPage = new ItemsList[5];
        private int mCurrentTab = -1;
        private Item mSelectedItem = null;

        public MainRect(float x, float y, float width, float height) {
            super(x, y);
            mWidth = width;
            mHeight = height;

            IM_HeadlineRect = new HeadlineRect(0,0,855, 50);
            addChild(IM_HeadlineRect);

            IM_InventoryList = new ListBox(-15, 75, 400, 468);
            IM_InventoryList.setSorted(true);
            addChild(IM_InventoryList);

            IM_RepairButton = new TextBox(855, 45+0, GameString.getString("sInventoryRepair"),
                    20, 15, Align.right, new Color(1,1,1,0.5f));
            addChild(IM_RepairButton);
            IM_ModButton = new TextBox(855, 45+47, GameString.getString("sInventoryMod"),
                    20, 15, Align.right, new Color(1,1,1,0.5f));
            addChild(IM_ModButton);

            IM_ItemIcon = new Image(450, 10, null, 380, 380);
            addChild(IM_ItemIcon);
            IM_ItemIconBadge = new Image(390, 260, null, 64, 64);
            addChild(IM_ItemIconBadge);

            IM_StrReq = new Text(440, 300, "", 7);
            addChild(IM_StrReq);

            IM_ItemInfoRect = new ItemStatsDisplay(385, 340, 446, 150, 4);
            addChild(IM_ItemInfoRect);

            IM_ItemInfoRect.setItem(null);
        }

        public void fillListWithItems() {
            IM_InventoryList.clearItems();
            ItemsList items = mItemsPage[mCurrentTab];
            if(items == null)
                return;
            boolean itemFound = false;
            for(Item item : items.list) {
                ItemListItem element = new ItemListItem(32,0, item.getNameWithAmount(false),
                        item.isEquippable(), item.isEquipped());
                element.addClickableListener(mItemClickListener, item);
                element.setClickSound("sound/fx/ui/pipboy/ui_pipboy_scroll.wav");
                IM_InventoryList.addItem(element);
                if(item.equivalent(mSelectedItem)) {
                    IM_InventoryList.highLightItem(element, true);
                    displayItem(item);
                    itemFound = true;
                }
            }
            if(!itemFound) {
                IM_ItemInfoRect.setItem(null);
                IM_ItemIcon.setFile(null);
                IM_ItemIconBadge.setFile(null);
            }
        }

        private void displayItem(Item item) {
            IM_ItemInfoRect.setItem(item);
            IM_ItemIcon.setFile(GameFileResolver.combineWithFallback(item.getIcon(),
                    Constants.FNF_IMAGE));
            IM_ItemIconBadge.setFile(GameFileResolver.combineWithFallback(item.getBadge(),
                    Constants.FNF_IMAGE,true));
        }

        private ClickableListener mItemClickListener = new ClickableListener() {
            @Override
            public void onClickableEvent(Control source, Object user) {
                if(!Item.class.isAssignableFrom(user.getClass()) ||
                        !ItemListItem.class.isAssignableFrom(source.getClass())) {
                    return;
                }
                Item item = (Item) user;
                ItemListItem element = (ItemListItem) source;
                mSelectedItem = item;
                IM_InventoryList.highLightItem(element, true);
                displayItem(item);
            }
        };

        @Override
        public void update(Context context) {
            IFalloutData data = context.foData;

            if(mCurrentTab != IM_Tabline.getCurrentTab()) {
                mCurrentTab = IM_Tabline.getCurrentTab();
                mSelectedItem = null;
                fillListWithItems();
                IM_InventoryList.highLightItem(-1);
                IM_ItemInfoRect.setItem(null);
                IM_ItemIcon.setFile(null);
                IM_ItemIconBadge.setFile(null);
                IM_StrReq.setVisible(false);
            }

            ItemsList items = data.getItems();
            if(!items.equivalent(mItems)) {
                mItems = items;
                mItemsPage[0] = items.getItemsOfType(WeaponItem.class); // Weapons
                mItemsPage[1] = items.getItemsOfType(ApparelItem.class); // Apparel
                mItemsPage[2] = items.getItemsOfType(AidItem.class); // Aid
                mItemsPage[3] = items.getItemsOfType(MiscItem.class); // Misc
                mItemsPage[4] = items.getItemsOfType(AmmoItem.class); // Ammo
                fillListWithItems();
            }
            super.update(context);
        }

        @Override
        public Rectangle getSize() {
            return new Rectangle(mX, mY, mWidth, mHeight);
        }
    }

}
