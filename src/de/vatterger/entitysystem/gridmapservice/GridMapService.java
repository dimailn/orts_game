package de.vatterger.entitysystem.gridmapservice;

import com.artemis.utils.Bag;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import de.vatterger.entitysystem.util.Constants;
import de.vatterger.entitysystem.util.Functions;

public class GridMapService {

	private static Bag<Bag<CategorizedBucket>> buckets;
	private static int cellSize;
	private static Rectangle flyWeightRectangle;
	
	static {
		init(Constants.XY_BOUNDS, Constants.EXPECTED_ENTITYCOUNT);
	}
	
	private GridMapService(){}
	
	public static void init(int expectedSize, int expectedEntityCount) {
		cellSize = Functions.optimalCellSize(expectedSize, expectedEntityCount);
		buckets = new Bag<Bag<CategorizedBucket>>(1);
		flyWeightRectangle = new Rectangle();
	}
	
	public static void insert(Vector2 v, Integer e, GridFlag gf) {
		getBucket(cell(v.x), cell(v.y)).add(e, gf);
	}

	public static void insert(Circle c, Integer e, GridFlag gf) {
		insert(Functions.circleToRectangle(c, flyWeightRectangle), e, gf);
	}

	public static void insert(Rectangle r, Integer e, GridFlag gf) {
		final int startX = cell(r.x), endX = cell(r.x+r.width);
		final int startY = cell(r.y), endY = cell(r.y+r.height);
		for (int x = startX; x <= endX; x++) {
			for (int y = startY; y <= endY; y++) {
				getBucket(x, y).add(e, gf);
			}
		}
	}

	public static CategorizedBucket getBucket(float wx, float wy){
		return getBucket(cell(wx),cell(wy));
	}
	
	private static CategorizedBucket getBucket(int cx, int cy){
		Bag<CategorizedBucket> bbx = buckets.safeGet(cx);
		if(bbx == null) {
			buckets.set(cx, bbx = new Bag<CategorizedBucket>(1));
		}
		CategorizedBucket by = bbx.safeGet(cy);
		if(by == null) {
			bbx.set(cy, by = new CategorizedBucket());
		}
		return by;
	}

	public static Bag<Integer> getEntities(GridFlag gf, Circle c, Bag<Integer> fillBag) {
		return getEntities(gf, Functions.circleToRectangle(c, flyWeightRectangle), fillBag);
	}
	
	public static Bag<Integer> getEntities(GridFlag gf, Rectangle r, Bag<Integer> fillBag) {
		int startX = cell(r.x), endX = cell(r.x+r.width);
		int startY = cell(r.y), endY = cell(r.y+r.height);
		for (int x = startX; x <= endX; x++) {
			for (int y = startY; y <= endY; y++) {
				getBucket(x, y).getAllWithSimilarFlag(gf, fillBag);
			}
		}
		return fillBag;
	}
	
	public static void clear() {
		int sx = buckets.size(), sy;
		CategorizedBucket bucket = null;
		for (int x = 0; x < sx; x++) {
			if (buckets.get(x) == null) {
				continue;
			} else {
				sy = buckets.get(x).size();
				for (int y = 0; y < sy; y++) {
					bucket = buckets.get(x).get(y);
					if (bucket != null) {
						bucket.clear();
					}
				}
			}
		}
	}
		
	private static int cell(float p) {
		return (int)(p >= 0 ? p/cellSize : 0);
	}
}
