package nl.robojan.real_pipboy.Connection.Packets;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

/**
 * Created by s120330 on 18-7-2015.
 */
public class SetWorldInfoPacket extends DataPacket {
    private String mLocationName;
    private short mYear;
    private byte mMonth, mDay, mHour, mMinute;
    private int mUsableWidth, mUsableHeight;
    private short mCellNWX, mCellNWY, mCellSEX, mCellSEY;
    private float mMapScale, mMapOffsetX, mMapOffsetY;
    private String mMapPath;

    public SetWorldInfoPacket(String locationName, short year, byte month, byte day, byte hour,
                              byte minute, int usableWidth, int usableHeight, short cellNWX,
                              short cellNWY, short cellSEX, short cellSEY, float mapScale,
                              float mapOffsetX, float mapOffsetY, String mapPath) {
        mLocationName = locationName;
        mYear = year;
        mMonth = month;
        mDay = day;
        mHour = hour;
        mMinute = minute;
        mUsableWidth = usableWidth;
        mUsableHeight = usableHeight;
        mCellNWX = cellNWX;
        mCellNWY = cellNWY;
        mCellSEX = cellSEX;
        mCellSEY = cellSEY;
        mMapScale = mapScale;
        mMapOffsetX = mapOffsetX;
        mMapOffsetY = mapOffsetY;
        mMapPath = mapPath;

        mHeader = new DataPacketHeader(PacketTypes.getInstance().getType(getClass()),
                getSize() - DataPacketHeader.getHeaderSize());
    }

    public SetWorldInfoPacket(byte[] data, int offset, int len) {
        super(new DataPacketHeader(data, offset, len));
        int headerSize = DataPacketHeader.getHeaderSize();
        readData(data, offset + headerSize, len - headerSize);
    }

    public SetWorldInfoPacket(DataPacketHeader header, byte[] data, int offset, int len) {
        super(header);
        readData(data, offset, len);
    }

    private void readData(byte[] data, int offset, int len) {
        Charset charset = Charset.forName("ISO-8859-1");
        ByteBuffer buffer = ByteBuffer.wrap(data, offset, len);
        mYear = buffer.getShort();
        mMonth = buffer.get();
        mDay = buffer.get();
        mHour = buffer.get();
        mMinute = buffer.get();
        int nameLen = buffer.getShort();
        mLocationName = new String(data, buffer.position(), nameLen, charset);
        buffer.position(buffer.position() + nameLen);
        mUsableWidth = buffer.getInt();
        mUsableHeight = buffer.getInt();
        mCellNWX = buffer.getShort();
        mCellNWY = buffer.getShort();
        mCellSEX = buffer.getShort();
        mCellSEY = buffer.getShort();
        mMapScale = buffer.getFloat();
        mMapOffsetX = buffer.getFloat();
        mMapOffsetY = buffer.getFloat();
        int mapLen = buffer.getShort();
        mMapPath = new String(data, buffer.position(), mapLen, charset).replace('\\', '/');
    }

    @Override
    public int getSize() {
        return DataPacketHeader.getHeaderSize() + 8 + mLocationName.length()
                + 30 + mMapPath.length() ;
    }

    @Override
    public byte[] getBytes() {
        CharsetEncoder enc = Charset.forName("ISO-8859-1").newEncoder();
        ByteBuffer buffer = ByteBuffer.allocate(getSize());
        mHeader.fillBytebuffer(buffer);
        buffer.putShort(mYear);
        buffer.put(mMonth);
        buffer.put(mDay);
        buffer.put(mHour);
        buffer.put(mMinute);
        buffer.putShort((short) mLocationName.length());
        enc.encode(CharBuffer.wrap(mLocationName), buffer, true);
        buffer.putInt(mUsableWidth);
        buffer.putInt(mUsableHeight);
        buffer.putShort(mCellNWX);
        buffer.putShort(mCellNWY);
        buffer.putShort(mCellSEX);
        buffer.putShort(mCellSEY);
        buffer.putFloat(mMapScale);
        buffer.putFloat(mMapOffsetX);
        buffer.putFloat(mMapOffsetY);
        buffer.putShort((short) mMapPath.length());
        enc.encode(CharBuffer.wrap(mMapPath), buffer, true);
        return buffer.array();
    }

    @Override
    public String toString() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        String time = formatter.format(getCalendar().getTime());

        return "SetWorldInfoPacket{Date=\"" + time +"\", Location=\"" + mLocationName + "\"}";
    }

    public GregorianCalendar getCalendar() {
        return new GregorianCalendar(mYear, mMonth, mDay, mHour, mMinute);
    }

    public String getLocationName() {
        return mLocationName;
    }

    public void setLocationName(String locationName) {
        mLocationName = locationName;
    }

    public short getYear() {
        return mYear;
    }

    public void setYear(short year) {
        mYear = year;
    }

    public byte getMonth() {
        return mMonth;
    }

    public void setMonth(byte month) {
        mMonth = month;
    }

    public byte getDay() {
        return mDay;
    }

    public void setDay(byte day) {
        mDay = day;
    }

    public byte getHour() {
        return mHour;
    }

    public void setHour(byte hour) {
        mHour = hour;
    }

    public byte getMinute() {
        return mMinute;
    }

    public void setMinute(byte minute) {
        mMinute = minute;
    }

    public int getUsableWidth() {
        return mUsableWidth;
    }

    public void setUsableWidth(int usableWidth) {
        mUsableWidth = usableWidth;
    }

    public int getUsableHeight() {
        return mUsableHeight;
    }

    public void setUsableHeight(int usableHeight) {
        mUsableHeight = usableHeight;
    }

    public short getCellNWX() {
        return mCellNWX;
    }

    public void setCellNWX(short cellNWX) {
        mCellNWX = cellNWX;
    }

    public short getCellNWY() {
        return mCellNWY;
    }

    public void setCellNWY(short cellNWY) {
        mCellNWY = cellNWY;
    }

    public short getCellSEX() {
        return mCellSEX;
    }

    public void setCellSEX(short cellSEX) {
        mCellSEX = cellSEX;
    }

    public short getCellSEY() {
        return mCellSEY;
    }

    public void setCellSEY(short cellSEY) {
        mCellSEY = cellSEY;
    }

    public float getMapScale() {
        return mMapScale;
    }

    public void setMapScale(float mapScale) {
        mMapScale = mapScale;
    }

    public float getMapOffsetX() {
        return mMapOffsetX;
    }

    public void setMapOffsetX(float mapOffsetX) {
        mMapOffsetX = mapOffsetX;
    }

    public float getMapOffsetY() {
        return mMapOffsetY;
    }

    public void setMapOffsetY(float mapOffsetY) {
        mMapOffsetY = mapOffsetY;
    }

    public String getMapPath() {
        return mMapPath;
    }

    public void setMapPath(String mapPath) {
        mMapPath = mapPath;
    }
}
