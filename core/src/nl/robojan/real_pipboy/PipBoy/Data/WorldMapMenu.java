package nl.robojan.real_pipboy.PipBoy.Data;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import nl.robojan.real_pipboy.Context;
import nl.robojan.real_pipboy.FalloutData.IFalloutData;
import nl.robojan.real_pipboy.FalloutData.MapMarker;
import nl.robojan.real_pipboy.FalloutData.MapMarkerList;
import nl.robojan.real_pipboy.PipBoy.Constants;
import nl.robojan.real_pipboy.PipBoy.Controls.Control;
import nl.robojan.real_pipboy.PipBoy.Controls.Image;
import nl.robojan.real_pipboy.PipBoy.Controls.Rect;
import nl.robojan.real_pipboy.PipBoy.Controls.TextBox;
import nl.robojan.real_pipboy.util.GameFileResolver;

/**
 * Created by s120330 on 3-8-2015.
 */
public class WorldMapMenu extends Rect {

    private static final String DEFAULT_WORLDMAP_TEXTURE = "textures/interface/worldmap/wasteland_nv_1024_no_map.dds";
    private static final String PLAYER_INDICATOR = "textures/interface/icons/misc/glow_cursor.dds";
    private static final float PLAYER_INDICATOR_ANGLE_OFFSET = 0.47f;
    private static final float ZOOM = 1.25f;
    private static final float CELL_SIZE = 4096 * ZOOM;

    private String mMapTexture = DEFAULT_WORLDMAP_TEXTURE;
    private Image mMap;
    private Vector2 mMapSize = new Vector2(2048, 2048);
    private float mMagnification = 0.5f;
    private float mMapScale = 1.0f;
    private float mMinMag = 0.1f;
    private float mMaxMag = 5f;

    private Image mPlayerIndicator;

    private Vector2 mZeroPos = new Vector2(0.5f, 0.5f);
    private Vector2 mTotalSize = new Vector2(1,1);
    private Vector2 mPixelsPerUnit = new Vector2(0,0);
    private Vector2 mMapOffset = new Vector2();

    private Vector2 mCellNW = new Vector2();
    private Vector2 mCellSE = new Vector2();
    private Vector2 mMapDimensions = mMapSize;

    private Vector3 mPlayerPos = new Vector3();
    private float mPlayerAngle = 0;
    private MapMarkerList mMapMarkers = new MapMarkerList();
    private Array<MapMarkerIcon> mMapMarkersImage = new Array<MapMarkerIcon>();
    private MapMarkerIcon mSelectedMarker = null;
    private MapHighlightBox mHighlightBox;

    private boolean mPanning = false;
    private Vector2 mOldMapPos= new Vector2();
    private Vector2 mOldMapSize= new Vector2();

    private boolean mVisible = true;

    public WorldMapMenu(float x, float y, float width, float height) {
        super(x, y, width, height);

        this.mClips = true;

        mMap = new Image(-256,-256,mMapTexture, mMapSize.x * mMagnification,
                mMapSize.y * mMagnification);
        mMinMag = mWidth / (mMapDimensions.x * 0.8f);
        mMaxMag = 5.0f;
        addChild(mMap);

        mPlayerIndicator = new Image(0.5f * mMap.getWidth(), 0.5f * mMap.getHeight(),
                PLAYER_INDICATOR, 48, 48*2);
        mMap.addChild(mPlayerIndicator);

        mHighlightBox = new MapHighlightBox(348, 170, 160, 160);
        mMap.addChild(mHighlightBox);
        mHighlightBox.setVisible(false);

        calculateMapPositions();
    }

    public void setSelectedMarker(MapMarkerIcon marker) {
        mSelectedMarker = marker;
        if(marker == null) {
            mHighlightBox.setVisible(false);
        } else {
            mHighlightBox.setVisible(true);
            mHighlightBox.setPos(marker.getPos());
            mHighlightBox.setText(marker.getMarker().getName());
        }
    }

    public void centerAroundPlayer() {
        mMagnification = 1.0f;
        setIconPositionsAndScale();
        mMap.setX(mWidth / 2 - mPlayerIndicator.getX());
        mMap.setY(mHeight / 2 - mPlayerIndicator.getY());
    }

    public void setVisible(boolean visible) {
        mVisible = visible;
        mMap.setVisible(visible);
        mPlayerIndicator.setVisible(visible);
        mHighlightBox.setVisible(visible && mSelectedMarker != null);
        for(MapMarkerIcon icon : mMapMarkersImage) {
            icon.setVisible(visible);
        }
        centerAroundPlayer();
    }

    public boolean isVisible() {
        return mVisible;
    }

    public Vector2 positionToMapPosition(Vector3 pos) {
        return new Vector2(mZeroPos.x * mMap.getWidth() + pos.x * mPixelsPerUnit.x,
                mZeroPos.y * mMap.getHeight() - pos.y *mPixelsPerUnit.y);
    }

    public void calculateMapPositions() {
        mTotalSize = new Vector2((mCellSE.x - mCellNW.x + 1) * CELL_SIZE / mMapScale,
                (mCellNW.y - mCellSE.y + 1) * CELL_SIZE / mMapScale);
        mZeroPos = new Vector2(((-mCellNW.x - 1) * CELL_SIZE + mMapOffset.x * ZOOM) / mTotalSize.x,
                ((mCellNW.y) *  CELL_SIZE - mMapOffset.y * ZOOM) / mTotalSize.y);
        mPixelsPerUnit = new Vector2(mMap.getWidth()/mTotalSize.x,
                mMap.getHeight()/mTotalSize.y);
    }

    public void setIconPositionsAndScale() {
        mPlayerIndicator.setOrigin(new Vector2(mPlayerIndicator.getWidth() * 0.41f,
                mPlayerIndicator.getHeight() * 0.28f));
        mPlayerIndicator.setPos(positionToMapPosition(mPlayerPos));
        mPlayerIndicator.setAngle(-mPlayerAngle - PLAYER_INDICATOR_ANGLE_OFFSET);
        for(MapMarkerIcon icon : mMapMarkersImage) {
            icon.updatePositionAndScale();
        }
    }

    @Override
    public void update(Context context) {
        super.update(context);
        IFalloutData data = context.foData;

        String texture = data.getWorldMapPath();
        if(texture.isEmpty()) texture = DEFAULT_WORLDMAP_TEXTURE;
        Vector2 cellNW = data.getWorldMapCellNW();
        Vector2 cellSE = data.getWorldMapCellSE();
        Vector2 mapDim = data.getWorldMapUsableDim();
        Vector2 mapOffset = data.getWorldMapOffset();
        float mapScale = data.getWorldMapScale();
        if(!cellSE.equals(mCellSE) || !cellNW.equals(mCellNW) || !mapDim.equals(mMapDimensions) ||
                !texture.equals(mMapTexture) || !mapOffset.equals(mMapOffset) ||
                mapScale != mMapScale)
        {
            if(!texture.equals(mMapTexture)) {
                mMapTexture = texture;
                mMap.setFile(GameFileResolver.combineWithFallback(mMapTexture, Constants.FNF_IMAGE));
                mMinMag = mWidth / (mapDim.x * 0.8f);
                mMaxMag = 5.0f;
                mMap.setWidth(mapDim.x * mMagnification);
                mMap.setHeight(mapDim.y * mMagnification);
            }
            // Something changed
            mCellNW = cellNW;
            mCellSE = cellSE;
            mMapDimensions = mapDim;
            mMapOffset = mapOffset;
            mMapScale = mapScale;
            calculateMapPositions();
            centerAroundPlayer();
        }

        mPlayerPos = data.getPlayerPos();
        mPlayerAngle = data.getPlayerRot().z;
        setIconPositionsAndScale();

        MapMarkerList markers = data.getMapMarkers();
        if(!mMapMarkers.equivalent(markers) && markers != null) {
            mMapMarkers = markers;

            removeAllMapMarkers();

            boolean foundSelected = false;
            for(MapMarker marker : mMapMarkers.markers) {
                MapMarkerIcon markerImage = new MapMarkerIcon(0,0, 48, 48, marker);
                markerImage.setVisible(mVisible);
                mMapMarkersImage.add(markerImage);
                mMap.addChild(markerImage, true);
                if(mSelectedMarker != null &&
                        marker.getName().equals(mSelectedMarker.getMarker().getName())) {
                    foundSelected = true;
                    mSelectedMarker = markerImage;
                }
            }
            if(!foundSelected) {
                setSelectedMarker(null);
            }
        }

        if(mSelectedMarker != null) {
            mHighlightBox.setPos(mSelectedMarker.getPos());
        }
    }

    public void removeAllMapMarkers() {
        for(MapMarkerIcon marker : mMapMarkersImage) {
            mMap.removeChild(marker);
            marker.dispose();
        }
        mMapMarkersImage.clear();
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        mOldMapPos = mMap.getPos();
        mOldMapSize = new Vector2(mMap.getWidth(), mMap.getHeight());
        return super.touchDown(x, y, pointer, button);
    }

    @Override
    public boolean pinch(Vector2 ip1, Vector2 ip2, Vector2 p1, Vector2 p2) {
        boolean handled = super.pinch(ip1, ip2, p1, p2);
        if(!handled && getSize().contains(ip1) && getSize().contains(ip2)) {
            handled = true;

            float id = new Vector2(ip1).sub(ip2).len();
            float d = new Vector2(p1).sub(p2).len();
            float zoom = d/id;

            mMagnification = mOldMapSize.x * zoom / mMapDimensions.x;
            if(mMagnification < mMinMag) {
                mMagnification = mMinMag;
            }
            if(mMagnification > mMaxMag) {
                mMagnification = mMaxMag;
            }

            mMap.setWidth(mMapDimensions.x * mMagnification);
            mMap.setHeight(mMapDimensions.y * mMagnification);
            zoom = mMagnification * mMapDimensions.x / mOldMapSize.x;

            float newX = (ip1.x - mOldMapPos.x) * zoom;
            float newY = (ip1.y - mOldMapPos.y) * zoom;
            float mapX = p1.x - newX;
            float mapY = p1.y - newY;

            mMap.setX(mapX);
            mMap.setY(mapY);
            calculateMapPositions();
        }
        return handled;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        boolean handled = super.pan(x, y, deltaX, deltaY);
        if(!handled && getSize().contains(x,y)) {
            mPanning = true;
            handled = true;
        }
        if(mPanning) {
            float mapx = mMap.getX() + deltaX;
            float mapy = mMap.getY() + deltaY;

            if(mapx < -mMap.getWidth() - 100 + getWidth()) {
                mapx = -mMap.getWidth() - 100 + getWidth();
            }
            if(mapx > 100) {
                mapx = 100;
            }

            if(mapy < -mMap.getHeight() - 100 + getHeight()) {
                mapy = -mMap.getHeight() - 100 + getHeight();
            }
            if(mapy > 100) {
                mapy = 100;
            }

            mMap.setX(mapx);
            mMap.setY(mapy);
        }
        return handled;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        mPanning = false;
        return super.panStop(x, y, pointer, button);
    }

    private class MapHighlightBox extends Rect {
        private static final String SOLID = "textures/interface/shared/solid.dds";
        private static final String FADE_TO_RIGHT = "textures/interface/shared/line/fade_to_right.dds";
        private static final String FADE_TO_LEFT = "textures/interface/shared/line/fade_to_left.dds";
        private static final String FADE_TO_UP = "textures/interface/shared/line/fade_to_top.dds";
        private static final String FADE_TO_DOWN = "textures/interface/shared/line/fade_to_bottom.dds";

        private TextBox mLocationName;
        private Image mULHoriz, mULVert, mURHoriz, mURVert, mLLHoriz, mLLVert, mLRHoriz, mLRVert;
        private Image mULHorizTip, mULVertTip, mURHorizTip, mURVertTip, mLLHorizTip, mLLVertTip,
            mLRHorizTip, mLRVertTip;

        public MapHighlightBox(float x, float y, float width, float height) {
            super(x, y, width, height);

            setOrigin(new Vector2(width/2, height/2));

            mLocationName = new TextBox(80, 180, "", Align.center);
            mLocationName.setFontIndex(1);
            addChild(mLocationName);

            mULHoriz = new Image(0, 0, SOLID, 40, Constants.LINE_THICKNESS);
            mULHorizTip = new Image(40, 0, FADE_TO_RIGHT, 15, Constants.LINE_THICKNESS);
            mULHoriz.addChild(mULHorizTip);
            addChild(mULHoriz);
            mULVert = new Image(0, 0, SOLID, Constants.LINE_THICKNESS, 40);
            mULVertTip = new Image(0, 40, FADE_TO_DOWN, Constants.LINE_THICKNESS, 15);
            mULVert.addChild(mULVertTip);
            addChild(mULVert);
            mURHoriz = new Image(120, 0, SOLID, 40, Constants.LINE_THICKNESS);
            mURHorizTip = new Image(-15, 0, FADE_TO_LEFT, 15, Constants.LINE_THICKNESS);
            mURHoriz.addChild(mURHorizTip);
            addChild(mURHoriz);
            mURVert = new Image(160 - Constants.LINE_THICKNESS, 0, SOLID,
                    Constants.LINE_THICKNESS, 40);
            mURVertTip = new Image(0, 40, FADE_TO_DOWN, Constants.LINE_THICKNESS, 15);
            mURVert.addChild(mURVertTip);
            addChild(mURVert);
            mLLHoriz = new Image(0, 160 - Constants.LINE_THICKNESS, SOLID, 40,
                    Constants.LINE_THICKNESS);
            mLLHorizTip = new Image(40, 0, FADE_TO_RIGHT, 15, Constants.LINE_THICKNESS);
            mLLHoriz.addChild(mLLHorizTip);
            addChild(mLLHoriz);
            mLLVert = new Image(0, 120, SOLID, Constants.LINE_THICKNESS, 40);
            mLLVertTip = new Image(0, -15, FADE_TO_UP, Constants.LINE_THICKNESS, 15);
            mLLVert.addChild(mLLVertTip);
            addChild(mLLVert);
            mLRHoriz = new Image(120, 160 - Constants.LINE_THICKNESS, SOLID, 40,
                    Constants.LINE_THICKNESS);
            mLRHorizTip = new Image(-15, 0, FADE_TO_LEFT, 15, Constants.LINE_THICKNESS);
            mLRHoriz.addChild(mLRHorizTip);
            addChild(mLRHoriz);
            mLRVert = new Image(160 - Constants.LINE_THICKNESS, 120, SOLID,
                    Constants.LINE_THICKNESS, 40);
            mLRVertTip = new Image(0, -15, FADE_TO_UP, Constants.LINE_THICKNESS, 15);
            mLRVert.addChild(mLRVertTip);
            addChild(mLRVert);
        }

        public void setText(String location) {
            mLocationName.setText(location);
        }

        public void setVisible(boolean visible) {
            mLocationName.setVisible(visible);
            mURHoriz.setVisible(visible);
            mURHorizTip.setVisible(visible);
            mURVert.setVisible(visible);
            mURVertTip.setVisible(visible);
            mULHoriz.setVisible(visible);
            mULHorizTip.setVisible(visible);
            mULVert.setVisible(visible);
            mULVertTip.setVisible(visible);
            mLRHoriz.setVisible(visible);
            mLRHorizTip.setVisible(visible);
            mLRVert.setVisible(visible);
            mLRVertTip.setVisible(visible);
            mLLHoriz.setVisible(visible);
            mLLHorizTip.setVisible(visible);
            mLLVert.setVisible(visible);
            mLLVertTip.setVisible(visible);
        }
        public boolean isVisible() {
            return mURHoriz.getVisible();
        }
    }

    private class MapMarkerIcon extends Image implements ClickableListener{
        private MapMarker mMarker;
        private float mXOffset, mYOffset;

        public MapMarkerIcon(float xOffset, float yOffset, float width, float height,
                             MapMarker marker) {
            super(xOffset, yOffset, marker.getIcon(!marker.canTravel()), width, height);
            mXOffset = xOffset;
            mYOffset = yOffset;
            mMarker = marker;
            setOrigin(new Vector2(width/2, height/2));
            setVisible(marker.isVisible());
            addClickableListener(this);
            updatePositionAndScale();
        }

        public void updatePositionAndScale() {
            Vector2 pos = positionToMapPosition(mMarker.getPos());
            pos.add(mXOffset, mYOffset);
            setPos(pos);
        }

        public void setSelected(boolean selected) {
            if(selected) {
                setWidth(72);
                setHeight(72);
                setOrigin(new Vector2(36, 36));
            } else {
                setWidth(48);
                setHeight(48);
                setOrigin(new Vector2(24, 24));
            }
        }

        @Override
        public void onClickableEvent(Control source, Object user) {
            if(!isVisible() || !isEnabled()) {
                return;
            }
            if(mSelectedMarker != null) {
                mSelectedMarker.setSelected(false);
                if(mSelectedMarker == this) {
                    setSelectedMarker(null);
                    return;
                }
            }
            setSelectedMarker(this);
            setSelected(true);
        }

        @Override
        public void setVisible(boolean visible) {
            super.setVisible(visible && mMarker.isVisible());
        }

        public MapMarker getMarker() {
            return mMarker;
        }

        public void setMarker(MapMarker marker) {
            mMarker = marker;
        }

        public float getXOffset() {
            return mXOffset;
        }

        public void setXOffset(float XOffset) {
            mXOffset = XOffset;
        }

        public float getYOffset() {
            return mYOffset;
        }

        public void setYOffset(float YOffset) {
            mYOffset = YOffset;
        }

    }
}
