package nl.robojan.real_pipboy.Connection.Packets;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import nl.robojan.real_pipboy.FalloutData.AidItem;
import nl.robojan.real_pipboy.FalloutData.AmmoItem;
import nl.robojan.real_pipboy.FalloutData.ApparelItem;
import nl.robojan.real_pipboy.FalloutData.Item;
import nl.robojan.real_pipboy.FalloutData.ItemsList;
import nl.robojan.real_pipboy.FalloutData.MiscItem;
import nl.robojan.real_pipboy.FalloutData.WeaponItem;

/**
 * Created by s120330 on 25-7-2015.
 */
public class SetInventoryPacket extends DataPacket {
    public static final int ITEMTYPE_UNK = 0;
    public static final int ITEMTYPE_WEAPON = 1;
    public static final int ITEMTYPE_AID = 2;
    public static final int ITEMTYPE_ARMOR = 3;
    public static final int ITEMTYPE_MISC = 4;
    public static final int ITEMTYPE_AMMO = 5;
    public static final int ITEMTYPE_IMOD = 6;
    public static final int ITEMTYPE_KEY = 7;

    public static final int ITEM_FLAG_EQUIPPABLE = 0x1;
    public static final int ITEM_FLAG_EQUIPPED = 0x2;

    private ItemsList mItems;

    public SetInventoryPacket(ItemsList items) {
        mItems = items;

        mHeader = new DataPacketHeader(PacketTypes.getInstance().getType(getClass()),
                getSize() - DataPacketHeader.getHeaderSize());
    }

    public SetInventoryPacket(byte[] data, int offset, int len) {
        super(new DataPacketHeader(data, offset, len));
        int headerSize = DataPacketHeader.getHeaderSize();
        readData(data, offset + headerSize, len - headerSize);
    }

    public SetInventoryPacket(DataPacketHeader header, byte[] data, int offset, int len) {
        super(header);
        readData(data, offset, len);
    }

    private void readData(byte[] data, int offset, int len) {
        ByteBuffer buffer = ByteBuffer.wrap(data, offset, len);
        Charset charset = Charset.forName("ISO-8859-1");

        mItems = new ItemsList();

        int numItems = buffer.getShort();
        for(int i = 0; i < numItems; i++) {
            int bufferStart = buffer.position();
            int itemSize = buffer.getShort();
            int itemType = buffer.get();
            long id  = buffer.getInt();
            int amount = buffer.getInt();
            float weight = buffer.getFloat();
            int value = buffer.getInt();
            int flags = buffer.get();
            String name = null;
            String icon = null;
            String badge = null;
            String effects = null;
            short nameLen = buffer.getShort();
            if(nameLen > 0)
                name = new String(data, buffer.position(), nameLen, charset);
            buffer.position(buffer.position() + nameLen);
            short iconLen = buffer.getShort();
            if(iconLen > 0)
                icon = new String(data, buffer.position(), iconLen, charset).replace('\\', '/');
            buffer.position(buffer.position() + iconLen);
            short badgeLen = buffer.getShort();
            if(badgeLen > 0)
                badge = new String(data, buffer.position(), badgeLen, charset).replace('\\', '/');
            buffer.position(buffer.position() + badgeLen);
            short effectsLen = buffer.getShort();
            if(effectsLen > 0)
                effects = new String(data, buffer.position(), effectsLen, charset);
            buffer.position(buffer.position() + effectsLen);

            boolean equippable = (flags & ITEM_FLAG_EQUIPPABLE) != 0;
            boolean equipped = (flags & ITEM_FLAG_EQUIPPED) != 0;

            buffer.getShort();

            float dps, dam, cnd, dr, dt;
            int strReq;
            switch(itemType) {

            default:
            case ITEMTYPE_UNK:
                mItems.add(new Item(id, name, amount, value, weight, icon, badge, equippable,
                        equipped, effects));
                break;
            case ITEMTYPE_WEAPON:
                dps = buffer.getFloat();
                dam = buffer.getFloat();
                cnd = buffer.getFloat();
                strReq = buffer.getInt();
                mItems.add(new WeaponItem(id,name, amount, value, weight, icon,badge, equippable,
                        equipped, dps, dam, cnd, strReq, effects));
                break;
            case ITEMTYPE_ARMOR:
                dr = buffer.getFloat();
                dt = buffer.getFloat();
                cnd = buffer.getFloat();
                mItems.add(new ApparelItem(id, name, amount, value, weight, icon,badge, equippable,
                        equipped, effects, dr, dt, cnd));
                break;
            case ITEMTYPE_AID:
                mItems.add(new AidItem(id, name, amount, value, weight, icon, badge, equippable,
                        equipped, effects));
                break;
            case ITEMTYPE_IMOD:
            case ITEMTYPE_KEY:
            case ITEMTYPE_MISC:
                mItems.add(new MiscItem(id, name, amount, value, weight, icon, badge, equippable,
                        equipped, effects));
                break;
            case ITEMTYPE_AMMO:
                mItems.add(new AmmoItem(id, name, amount, value, weight, icon, badge, equippable,
                        equipped, effects));
                break;
            }
            buffer.position(bufferStart + itemSize);
        }
    }

    @Override
    public int getSize() {
        int itemsSize = 0;

        for(Item item : mItems.list) {
            itemsSize += 2 + 28;
            if(item.getName() != null)
                itemsSize += item.getName().length();
            if(item.getIcon() != null)
                itemsSize += item.getIcon().length();
            if(item.getBadge() != null)
                itemsSize += item.getBadge().length();
            if(item.getEffect() != null)
                itemsSize += item.getEffect().length();
            if(item instanceof WeaponItem) {
                itemsSize += 16;
            } else if(item instanceof ApparelItem) {
                itemsSize += 12;
            } else if(item instanceof AidItem) {
                itemsSize += 0;
            } else if(item instanceof MiscItem) {
                itemsSize += 0;
            } else if(item instanceof AmmoItem) {
                itemsSize += 0;
            }
        }

        return DataPacketHeader.getHeaderSize() + 2 + itemsSize;
    }

    @Override
    public byte[] getBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(getSize());
        CharsetEncoder enc = Charset.forName("ISO-8859-1").newEncoder();
        mHeader.fillBytebuffer(buffer);

        buffer.putShort((short) mItems.list.size);

        for(Item item : mItems.list) {
            int detailSize = 0;
            int itemSize = 28;
            int itemType = ITEMTYPE_UNK;
            if(item instanceof WeaponItem) {
                detailSize += 16;
                itemType = ITEMTYPE_WEAPON;
            } else if(item instanceof ApparelItem) {
                detailSize += 12;
                itemType = ITEMTYPE_ARMOR;
            } else if(item instanceof AidItem) {
                detailSize += 0;
                itemType = ITEMTYPE_AID;
            } else if(item instanceof MiscItem) {
                detailSize += 0;
                itemType = ITEMTYPE_MISC;
            } else if(item instanceof AmmoItem) {
                detailSize += 0;
                itemType = ITEMTYPE_AMMO;
            }

            if(item.getName() != null) itemSize += item.getName().length();
            if(item.getIcon() != null) itemSize += item.getIcon().length();
            if(item.getBadge() != null) itemSize += item.getBadge().length();
            if(item.getEffect() != null) itemSize += item.getEffect().length();

            int bufferStart = buffer.position();
            itemSize += detailSize;
            buffer.putShort((short) itemSize);
            buffer.put((byte) itemType);
            buffer.putInt((int) item.getId());
            buffer.putInt(item.getAmount());
            buffer.putFloat(item.getWeight());
            buffer.putInt(item.getValue());
            byte flags = 0;
            if(item.isEquippable()) flags |= ITEM_FLAG_EQUIPPABLE;
            if(item.isEquipped()) flags |= ITEM_FLAG_EQUIPPED;
            buffer.put(flags);
            if(item.getName() == null) {
                buffer.putShort((short)0);
            } else {
                buffer.putShort((short)item.getName().length());
                enc.encode(CharBuffer.wrap(item.getName()), buffer, true);
            }
            if(item.getIcon() == null) {
                buffer.putShort((short)0);
            } else {
                buffer.putShort((short)item.getIcon().length());
                enc.encode(CharBuffer.wrap(item.getIcon()), buffer, true);
            }
            if(item.getBadge() == null) {
                buffer.putShort((short)0);
            } else {
                buffer.putShort((short)item.getBadge().length());
                enc.encode(CharBuffer.wrap(item.getBadge()), buffer, true);
            }
            if(item.getEffect() == null) {
                buffer.putShort((short)0);
            } else {
                buffer.putShort((short)item.getEffect().length());
                enc.encode(CharBuffer.wrap(item.getEffect()), buffer, true);
            }
            buffer.putShort((short)detailSize);
            switch(itemType) {
                case ITEMTYPE_WEAPON:
                    buffer.putFloat(((WeaponItem)item).getDPS());
                    buffer.putFloat(((WeaponItem)item).getDAM());
                    buffer.putFloat(((WeaponItem)item).getCondition());
                    buffer.putInt(((WeaponItem)item).getStrReq());
                    break;
                case ITEMTYPE_ARMOR:
                    buffer.putFloat(((ApparelItem) item).getDR());
                    buffer.putFloat(((ApparelItem) item).getDT());
                    buffer.putFloat(((ApparelItem) item).getCondition());
                    break;
            }
            buffer.position(bufferStart + itemSize + 2);
        }
        return buffer.array();
    }

    @Override
    public String toString() {
        return "SetInventoryPacket{items=" + Integer.toString(mItems.list.size) + "}";
    }

    public ItemsList getItems() {
        return mItems;
    }

    public void setItems(ItemsList items) {
        mItems = items;
    }
}
