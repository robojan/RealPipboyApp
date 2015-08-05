package nl.robojan.real_pipboy.Connection.Packets;

import com.badlogic.gdx.utils.Array;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import nl.robojan.real_pipboy.FalloutData.Effect;
import nl.robojan.real_pipboy.FalloutData.EffectInfo;
import nl.robojan.real_pipboy.FalloutData.StatusEffects;

/**
 * Created by s120330 on 20-7-2015.
 */
public class SetPlayerEffectsPacket extends DataPacket {
    private Array<Effect> mRadEffects;
    private Array<Effect> mH2oEffects;
    private Array<Effect> mFodEffects;
    private Array<Effect> mSlpEffects;
    private StatusEffects mPlayerEffects;

    public SetPlayerEffectsPacket(Array<Effect> radEffects, Array<Effect> h2oEffects,
                                  Array<Effect> fodEffects, Array<Effect> slpEffects,
                                  StatusEffects playerEffects) {
        mRadEffects = radEffects;
        mH2oEffects = h2oEffects;
        mFodEffects = fodEffects;
        mSlpEffects = slpEffects;
        mPlayerEffects = playerEffects;

        mHeader = new DataPacketHeader(PacketTypes.getInstance().getType(getClass()),
                getSize() - DataPacketHeader.getHeaderSize());
    }

    public SetPlayerEffectsPacket(byte[] data, int offset, int len) {
        super(new DataPacketHeader(data, offset, len));
        int headerSize = DataPacketHeader.getHeaderSize();
        readData(data, offset + headerSize, len - headerSize);
    }

    public SetPlayerEffectsPacket(DataPacketHeader header, byte[] data, int offset, int len) {
        super(header);
        readData(data, offset, len);
    }

    private void readData(byte[] data, int offset, int len) {
        Charset charset = Charset.forName("ISO-8859-1");
        ByteBuffer buffer = ByteBuffer.wrap(data, offset, len);
        // Read effects
        mRadEffects = new Array<Effect>();
        readEffects(mRadEffects, buffer, charset);
        mH2oEffects = new Array<Effect>();
        readEffects(mH2oEffects, buffer, charset);
        mFodEffects = new Array<Effect>();
        readEffects(mFodEffects, buffer, charset);
        mSlpEffects = new Array<Effect>();
        readEffects(mSlpEffects, buffer, charset);
        // Read player effects
        mPlayerEffects = new StatusEffects();
        int numPlayerEffects = buffer.getShort();
        for(int i = 0; i<numPlayerEffects; i++) {
            int nameLen = buffer.getShort();
            String name = new String(buffer.array(), buffer.arrayOffset() + buffer.position(),
                    nameLen, charset);
            buffer.position(buffer.position() + nameLen);
            int effectsLen = buffer.getShort();
            String effects = new String(buffer.array(), buffer.arrayOffset() + buffer.position(),
                    effectsLen, charset);
            buffer.position(buffer.position() + effectsLen);
            mPlayerEffects.add(new EffectInfo(name, effects));
        }
    }

    private void readEffects(Array<Effect> result, ByteBuffer buffer, Charset charset) {
        int len = buffer.get();
        for(int i = 0; i<len; i++) {
            int abbrevLen = buffer.getShort();
            String abbrev = new String(buffer.array(), buffer.arrayOffset() + buffer.position(),
                    abbrevLen, charset);
            buffer.position(buffer.position() + abbrevLen);
            int value = buffer.get();
            result.add(new Effect(abbrev, value));
        }
    }

    @Override
    public int getSize() {
        int radSize = 1;
        for(Effect e : mRadEffects) {
            radSize += 2 + e.abbrev.length() + 1;
        }
        int h2oSize = 1;
        for(Effect e : mRadEffects) {
            h2oSize += 2 + e.abbrev.length() + 1;
        }
        int fodSize = 1;
        for(Effect e : mRadEffects) {
            fodSize += 2 + e.abbrev.length() + 1;
        }
        int slpSize = 1;
        for(Effect e : mRadEffects) {
            slpSize += 2 + e.abbrev.length() + 1;
        }
        int effectSize = 2;
        for(EffectInfo info : mPlayerEffects.effects) {
            effectSize += 2 + info.name.length() + 2 + info.effects.length();
        }
        return DataPacketHeader.getHeaderSize() + radSize + h2oSize + fodSize + slpSize +
                effectSize;
    }

    private void putBytesEffect(ByteBuffer buffer, Array<Effect> effects, CharsetEncoder encoder) {
        buffer.put((byte)effects.size);
        for(Effect e : effects) {
            buffer.putShort((short)e.abbrev.length());
            encoder.encode(CharBuffer.wrap(e.abbrev), buffer, true);
            buffer.put((byte)e.effect);
        }
    }

    @Override
    public byte[] getBytes() {
        CharsetEncoder enc = Charset.forName("ISO-8859-1").newEncoder();
        ByteBuffer buffer = ByteBuffer.allocate(getSize());
        mHeader.fillBytebuffer(buffer);
        putBytesEffect(buffer, mRadEffects, enc);
        putBytesEffect(buffer, mH2oEffects, enc);
        putBytesEffect(buffer, mFodEffects, enc);
        putBytesEffect(buffer, mSlpEffects, enc);
        buffer.putShort((short) mPlayerEffects.effects.size);
        for(EffectInfo info : mPlayerEffects.effects) {
            buffer.putShort((short)info.name.length());
            enc.encode(CharBuffer.wrap(info.name), buffer, true);
            buffer.putShort((short) info.effects.length());
            enc.encode(CharBuffer.wrap(info.effects), buffer, true);
        }
        return buffer.array();
    }

    @Override
    public String toString() {
        return "SetPlayerEffectsPacket{rad=" + mRadEffects.size + ", H2O=" + + mH2oEffects.size +
                ", FOD="+ mFodEffects.size + ", SLP=" + mSlpEffects.size + ", Player=" +
                mPlayerEffects.effects.size + "}";
    }

    public Array<Effect> getRadEffects() {
        return mRadEffects;
    }

    public void setRadEffects(Array<Effect> radEffects) {
        mRadEffects = radEffects;
    }

    public Array<Effect> getH2oEffects() {
        return mH2oEffects;
    }

    public void setH2oEffects(Array<Effect> h2oEffects) {
        mH2oEffects = h2oEffects;
    }

    public Array<Effect> getFodEffects() {
        return mFodEffects;
    }

    public void setFodEffects(Array<Effect> fodEffects) {
        mFodEffects = fodEffects;
    }

    public Array<Effect> getSlpEffects() {
        return mSlpEffects;
    }

    public void setSlpEffects(Array<Effect> slpEffects) {
        mSlpEffects = slpEffects;
    }

    public StatusEffects getPlayerEffects() {
        return mPlayerEffects;
    }

    public void setPlayerEffects(StatusEffects playerEffects) {
        mPlayerEffects = playerEffects;
    }
}
