package nl.robojan.real_pipboy.PipBoy.Data;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import nl.robojan.real_pipboy.Context;
import nl.robojan.real_pipboy.FalloutData.IFalloutData;
import nl.robojan.real_pipboy.FalloutData.MapMarker;
import nl.robojan.real_pipboy.FalloutData.MapMarkerList;
import nl.robojan.real_pipboy.PipBoy.Constants;
import nl.robojan.real_pipboy.PipBoy.Controls.Image;
import nl.robojan.real_pipboy.PipBoy.Controls.Rect;
import nl.robojan.real_pipboy.RenderContext;
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

    private boolean mVisible = true;

    public WorldMapMenu(float x, float y, float width, float height) {
        super(x, y, width, height);

        this.mClips = true;

        mMap = new Image(-256,-256,mMapTexture, mMapSize.x * mMagnification,
                mMapSize.y * mMagnification);
        addChild(mMap);

        mPlayerIndicator = new Image(0.5f * mMap.getWidth(), 0.5f * mMap.getHeight(),
                PLAYER_INDICATOR, 48, 48*2);
        mMap.addChild(mPlayerIndicator);



        calculateMapPositions();
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

            for(MapMarker marker : mMapMarkers.markers) {
                MapMarkerIcon markerImage = new MapMarkerIcon(0,0, 32, 32, marker);
                mMapMarkersImage.add(markerImage);
                mMap.addChild(markerImage, true);
            }
        }
    }

    public void removeAllMapMarkers() {
        for(MapMarkerIcon marker : mMapMarkersImage) {
            mMap.removeChild(marker);
            marker.dispose();
        }
        mMapMarkersImage.clear();
    }

    private class MapMarkerIcon extends Image{
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
            updatePositionAndScale();
        }

        public void updatePositionAndScale() {
            Vector2 pos = positionToMapPosition(mMarker.getPos());
            pos.add(mXOffset, mYOffset);
            setPos(pos);
        }

        @Override
        public void setVisible(boolean visible) {
            super.setVisible(visible && mMarker.isVisible());
        }
    }
}
