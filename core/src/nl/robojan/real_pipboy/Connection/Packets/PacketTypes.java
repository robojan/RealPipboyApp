package nl.robojan.real_pipboy.Connection.Packets;

import com.badlogic.gdx.Gdx;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by s120330 on 16-7-2015.
 */
public class PacketTypes {

    private static PacketTypes ourInstance = new PacketTypes();

    public static PacketTypes getInstance() {
        return ourInstance;
    }

    private Map<Byte, Class> mPacketMap;
    private Map<Class, Byte> mPacketMapReverse;

    private PacketTypes() {
        mPacketMap = new HashMap<Byte, Class>();
        mPacketMapReverse = new HashMap<Class, Byte>();
    }

    public void registerClass(byte type, Class cls) {
        assert(DataPacket.class.isAssignableFrom(cls));

        mPacketMap.put(type,cls);
        mPacketMapReverse.put(cls,type);
    }

    public boolean contains(byte type) {
        return mPacketMap.containsKey(type);
    }

    public Class getClass(byte type) {
        return mPacketMap.get(type);
    }

    public byte getType(Class cls) {
        Byte type = mPacketMapReverse.get(cls);
        if(type == null)
            return 0;
        return type;
    }

    public DataPacket getDataPacket(DataPacketHeader header, byte[] data, int offset, int len) {
        Class<?> cls = getClass(header.getType());
        if(cls != null) {
            try {
                Constructor<?> ctor = cls.getConstructor(DataPacketHeader.class, byte[].class,
                        int.class, int.class);
                return (DataPacket)ctor.newInstance(header, data, offset, len);
            } catch (NoSuchMethodException e) {
                Gdx.app.error("PACKET", "Could not find packet with type: " +
                        Byte.toString(header.getType()) + "{" + e.getMessage() + "}");

                e.printStackTrace();

            }  catch (InstantiationException e) {
                Gdx.app.error("PACKET", "Could not create packet with type: " +
                        Byte.toString(header.getType()) + "{" + e.getMessage() + "}");
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                Gdx.app.error("PACKET", "Could not create packet with type: " +
                        Byte.toString(header.getType()) + "{" + e.getMessage() + "}");
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                Gdx.app.error("PACKET", "Could not create packet with type: " +
                        Byte.toString(header.getType()) + "{" + e.getMessage() + "}");
                e.printStackTrace();
            }
        }
        return new DataPacket(header, data, offset, len);
    }
}
