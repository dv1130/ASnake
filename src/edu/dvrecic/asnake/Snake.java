package edu.dvrecic.asnake;

import java.util.ArrayList;
import java.util.Random;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Snake extends SurfaceView implements Runnable{

	// deklaracija spremenljivk za smer kace
	private static final int SEVER = 1;
	private static final int JUG = 2;
	private static final int ZAHOD = 3;
	private static final int VZHOD = 4;
	private int smer = JUG;
	public int naslednjaSmer = ZAHOD;

	// deklaracija spremenljivk za Bitmap
	int velikostBitmap;
	private Bitmap[] poljeBitmap; // polje za shranjevanje Bitmap-ov
	private static final int TELO = 1;
	private static final int GLAVA_LEVO = 2;
	private static final int GLAVA_DESNO = 3;
	private static final int GLAVA_GOR = 4;
	private static final int GLAVA_DOL = 5;
	private static final int MIS = 6;
	private static final int JAJCE = 7;

	// deklaracija spremenljivk za stanje
	int stanje = PRIPRAVLJEN;
	public static final int ODMOR = 0;
	public static final int PRIPRAVLJEN = 1;
	public static final int IGRA_TECE = 2;
	public static final int IZGUBIL = 3;

	// deklaracija spremenljivk za igro
	private ArrayList<KoordinateXY> kaca = new ArrayList<KoordinateXY>();
	private ArrayList<KoordinateXY> miske = new ArrayList<KoordinateXY>();
	private ArrayList<KoordinateXY> jajca = new ArrayList<KoordinateXY>();
	private static final Random randomSt = new Random();

	private MediaPlayer eatingSound;

	Bitmap telo, glavaGor, glavaDol,
	glavaDesno, glavaLevo, mis, jajce;
	int stX, stY;
	boolean povecajKaco = false;
	boolean stMisiVecKot10 = false; // bonuc jajce + 10 misi
	int razmik; // razmik izrisa
	boolean desno = false;
	private Game parentActivity;
	///private static int odmikX; // odmik izrisa po X
	//private static int odmikY; // odmik izrisa po Y;
	private int[][] polje2D;
	int rezultat = 0;
	int maxRezultat;
	int hitrost = 300;
	int staraHitrost = 300;

	Thread t = null;
	SurfaceHolder holder;
	boolean isItOK = false;
	Typeface tfScore, tfGame;
	private static Context CONTEXT;

	public Snake(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		holder = getHolder();
		velikostBitmap = 32;
		//Nastavitev bitmap-ov
		telo = BitmapFactory.decodeResource(getResources(), R.drawable.telo);
		telo = Bitmap.createScaledBitmap(telo, velikostBitmap, velikostBitmap, false);
		glavaLevo = BitmapFactory.decodeResource(getResources(), R.drawable.glavalevo);
		glavaLevo = Bitmap.createScaledBitmap(glavaLevo, velikostBitmap, velikostBitmap, false);
		glavaDesno = BitmapFactory.decodeResource(getResources(), R.drawable.glavadesno);
		glavaDesno = Bitmap.createScaledBitmap(glavaDesno, velikostBitmap, velikostBitmap, false);
		glavaGor = BitmapFactory.decodeResource(getResources(), R.drawable.glavagor);
		glavaGor = Bitmap.createScaledBitmap(glavaGor, velikostBitmap, velikostBitmap, false);
		glavaDol = BitmapFactory.decodeResource(getResources(), R.drawable.glavadol);
		glavaDol = Bitmap.createScaledBitmap(glavaDol, velikostBitmap, velikostBitmap, false);
		mis = BitmapFactory.decodeResource(getResources(), R.drawable.mis);
		mis = Bitmap.createScaledBitmap(mis, velikostBitmap, velikostBitmap, false);
		jajce = BitmapFactory.decodeResource(getResources(), R.drawable.egg);
		jajce = Bitmap.createScaledBitmap(jajce, velikostBitmap, velikostBitmap, false);

		//nastavitev za poljeBitmap
		poljeBitmap = new Bitmap[9];
		poljeBitmap[1] = telo;
		poljeBitmap[2] = glavaLevo;
		poljeBitmap[3] = glavaDesno;
		poljeBitmap[4] = glavaGor;
		poljeBitmap[5] = glavaDol;
		poljeBitmap[6] = mis;
		poljeBitmap[7] = jajce;

		// inicializacija spremenljivk
		razmik = 25;
		stX = stY = 5;
		rezultat = 0;

		eatingSound = MediaPlayer.create(context, R.raw.pew);
		eatingSound.setLooping(false);
		eatingSound.setVolume(60, 60);

		tfScore = Typeface.createFromAsset(context.getAssets(),"data/fonts/chicken_butt.ttf");
		tfGame = Typeface.createFromAsset(context.getAssets(),"data/fonts/eye.TTF");

		CONTEXT = context;
		
		
		/*		if (OrientationManager.isSupported()) {
			OrientationManager.startListening(this);
		}*/

	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		stX = (int) Math.floor(w / razmik);
		stY = (int) Math.floor(h / razmik);
		System.out.println("w="+w+", h="+h);
		System.out.println("oldw"+oldw+", oldh="+oldh);
		System.out.println("stx="+stX+", stY="+stY);
		//odmikX = ((w - (razmik * stX)) / 2);
		//odmikY = ((h - (razmik * stY)) / 2);
		polje2D = new int[stX][stY];
		for(int i = 0; i<stX; i++){
			for(int j = 0; j<stY;j++){
				polje2D[i][j] = 0;
			}
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		//sprite = new Sprite(OurView.this, body);
		while(isItOK == true)
		{
			if(!holder.getSurface().isValid()){
				continue;
			}
			Canvas c = holder.lockCanvas();
			onDraw(c);
			holder.unlockCanvasAndPost(c);

		}
	}

	protected void onDraw(Canvas c){
		try {
			Thread.sleep(hitrost);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	

		c.drawARGB(255,255,255,255);
		Paint paint = new Paint(); 
		//paint.setColor(Color.WHITE); 
		paint.setStyle(Style.STROKE); 
		//c.drawPaint(paint); 
		paint.setColor(Color.BLACK); 
		paint.setAntiAlias(true);
		paint.setTextSize(48); 
		paint.setTypeface(tfScore);
		String aString = Integer.toString(rezultat);
		c.drawText("Score: "+aString, 10, 35, paint); 

		if(stanje == PRIPRAVLJEN)
		{
			paint.setTypeface(tfGame);
			paint.setTextSize(50); 
			c.drawText("TOUCH 4 START", c.getHeight()/2-50, c.getWidth()/4, paint);
		}
		else if(stanje == IZGUBIL)
		{
			paint.setTypeface(tfGame);
			paint.setTextSize(50); 
			c.drawText("GAME OVER", c.getHeight()/2, c.getWidth()/4, paint);	
		}

		//sprite.onDraw(c);
		osvezi();
		for (int x = 0; x < stX; x++) {
			for (int y = 0; y < stY; y++) {
				if(polje2D[x][y]>0)
				{
					c.drawBitmap(poljeBitmap[polje2D[x][y]], 
							x * razmik,
							y * razmik,
							null);
				}
			}
		}
	}
	public void pause(){
		Log.w("gameview","pause");
		if(rezultat > maxRezultat)
			maxRezultat = rezultat;
		isItOK = false;
		while(true){
			try{
				t.join();
			} catch(InterruptedException e){

				e.printStackTrace();
			}
			break;
		}
		t = null;
	}
	public void start(){
	}
	public void resume(){
		isItOK = true;
		Log.w("gameview","resume");
		t = new Thread(this);
		t.start();
	}

	public void stop(){
		Log.w("gameview","stop");
		//t.stop();
		//t.stop();
	}

	public void destroy(){
		Log.w("gameview","destroy");
		t.destroy();
		//t.stop();
	}
	//###### KACA ################################################
	private class KoordinateXY {
		public int x;
		public int y;

		public KoordinateXY(int novX, int novY) {
			x = novX;
			y = novY;
		}
		// primerjamo e sta x-x1 in y-y1 enaka
		public boolean primerjaj(KoordinateXY other) {
			if (x == other.x && y == other.y) {
				return true;
			}
			return false;
		}

		@Override
		public String toString() {
			return "KoordinateXY: [" + x + "," + y + "]";
		}
	}

	private void initNovaIgra(){
		kaca.clear();
		kaca.add(new KoordinateXY(3, 3));
		kaca.add(new KoordinateXY(3, 4));
		int st = miske.size();
		for(int i=0; i<st; i++)
			miske.remove(i);
		int st1 = jajca.size();
		for(int i=0; i<st1; i++)
			jajca.remove(i);
		dodajMis();
		dodajJajce();
		rezultat = 0;
		hitrost = 200;
		naslednjaSmer = ZAHOD;
	}

	public void pocistiMapo() {
		for (int x = 0; x < stX; x++) {
			for (int y = 0; y < stY; y++) {
				osveziPolje(0, x, y);
			}
		}
	}

	public void osvezi()
	{
		if (stanje == IGRA_TECE) {
			pocistiMapo();
			osveziMapo();
			for (KoordinateXY c : miske) {
				osveziPolje(MIS, c.x, c.y);
			}
			for (KoordinateXY c1 : jajca) {
				osveziPolje(JAJCE, c1.x, c1.y);
			}
		}
	}

	private void dodajMis() {

		int stMisi = miske.size();
		int novX;
		int novY;
		boolean trk = false;
		KoordinateXY noveCoord = null;
		boolean obstaja = false;
		int dolzinaKace = kaca.size();
		System.out.println("stMisi "+stMisi);

		while (!obstaja) {
			// generiramo nakljuène koodrinate za vabo
			novX = 1 + randomSt.nextInt(stX-1);
			novY = 1 + randomSt.nextInt(stY-1);
			noveCoord = new KoordinateXY(novX, novY);
			// preverimo èe kaèa že ni na vabi
			trk = false;
			for (int i = 0; i < dolzinaKace; i++) {
				if (kaca.get(i).primerjaj(noveCoord)) {
					trk = true;
				}
			}
			obstaja = !trk;
		}
		if (noveCoord == null) {
			Log.w("MIS", "Mis error");
		}
		miske.add(noveCoord);
	}

	private void dodajJajce() {

		KoordinateXY noveCoord = null;
		boolean obstaja = false;
		while (!obstaja) {
			// generiramo nakljuène koodrinate za vabo
			System.out.println("stX"+stX+", stY"+stY);
			int novX = 1 + randomSt.nextInt(stX-2);
			int novY = 1 + randomSt.nextInt(stY-2);
			noveCoord = new KoordinateXY(novX, novY);
			// preverimo èe kaèa že ni na vabi
			boolean trk = false;
			int dolzinaKace = kaca.size();
			for (int i = 0; i < dolzinaKace; i++) {
				if (kaca.get(i).primerjaj(noveCoord)) {
					trk = true;
				}
			}
			obstaja = !trk;
		}
		if (noveCoord == null) {
			Log.w("EGG", "EGG error3243");
		}
		jajca.add(noveCoord);

	}

	private void osveziMapo() {
		povecajKaco = false;
		KoordinateXY a = kaca.get(0);
		KoordinateXY glava = new KoordinateXY(1, 1);
		smer = naslednjaSmer;

		switch (smer) {
		case VZHOD: {
			glava = new KoordinateXY(a.x + 1, a.y);
			break;
		}
		case ZAHOD: {
			glava = new KoordinateXY(a.x - 1, a.y);
			break;
		}
		case SEVER: {
			glava = new KoordinateXY(a.x, a.y - 1);
			break;
		}
		case JUG: {
			glava = new KoordinateXY(a.x, a.y + 1);
			break;
		}
		}
		if(glava.x < 0)
		{
			glava = new KoordinateXY(stX-1 , a.y);
		}
		// zgoraj - spodaj
		if(glava.y < 0)
		{
			glava = new KoordinateXY(a.x , stY-1);
		}
		if(glava.x > stX - 1)
		{
			glava = new KoordinateXY(0, a.y);
		}
		// spodaj - zgoraj
		if(glava.y > stY - 1)
		{
			glava = new KoordinateXY(a.x, 0);
		}	
		// ce se zaletimo potem izgubimo
		int dolzinaKace = kaca.size();
		for (int i = 0; i < dolzinaKace; i++) {
			KoordinateXY c = kaca.get(i);
			if (c.primerjaj(glava)) {
				stanje = IZGUBIL;	
				if(rezultat > maxRezultat)
					maxRezultat = rezultat;
				//return;
			}
		}
		int stMisi = miske.size();
		int stJajc = jajca.size();

		for (int m = 0; m < stMisi; m++) {
			KoordinateXY c = miske.get(m);
			if (c.primerjaj(glava)) {
				miske.remove(c);
				stMisi = miske.size();
				m=0;
				if(stMisi < 2)
				{
					dodajMis();
				}
				eatingSound.start();
				if(hitrost > 100)
					hitrost = hitrost - 5;
				else if(hitrost > 5)
					hitrost = hitrost - 2;
				else
					hitrost = 0;

				rezultat++;
				povecajKaco = true;
			}
		}

		for(int j = 0; j < stJajc; j++){
			KoordinateXY cordJajce = jajca.get(j);		
			if(cordJajce.primerjaj(glava))
			{
				hitrost = staraHitrost;
				System.out.println(hitrost);
				int r = randomSt.nextInt(10);
				if(r == 0){
					staraHitrost = hitrost;
					hitrost = 0;
					rezultat++;
				}else if( r == 1){
					staraHitrost = hitrost;
					hitrost = 800;
					rezultat++;
				}else if(r == 2){
					if(stMisi < 3){
						for(int k = 0; k<20; k++)
						dodajMis();
					}
				}else if(r == 3){
					rezultat = rezultat * 2;
				}
				jajca.remove(cordJajce);
				eatingSound.start();
				dodajJajce();
			}
		}
		// premikamo kaco na nove koordinate
		kaca.add(0, glava);
		if (!povecajKaco) {			
			kaca.remove(kaca.size() - 1);

		}
		int i = 0;
		for (KoordinateXY c : kaca) {
			if (i == 0) {

				if(smer == ZAHOD){
					osveziPolje(GLAVA_LEVO, c.x, c.y);
				}
				else if(smer == VZHOD)
				{
					osveziPolje(GLAVA_DESNO, c.x, c.y);
				}
				else if(smer == SEVER){
					osveziPolje(GLAVA_GOR, c.x, c.y);
				}
				else if(smer == JUG){
					osveziPolje(GLAVA_DOL, c.x, c.y);
				}

			} else {
				osveziPolje(TELO, c.x, c.y);
			}
			i++;
		}
	}

	public Game getParentActivity() {
		return parentActivity;
	}

	public void setParentActivity(Game parentActivity) {
		this.parentActivity = parentActivity;
	}


	private void osveziPolje(int key, int x, int y){
		polje2D[x][y] = key;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		float x = event.getX();
		float y = event.getY();
		float height = 404;
		float width = 800;
		float slope = height/width;

		// Only process DOWN action, so it responds as soon as the
		// screen is touched.
		if (event.getAction()==MotionEvent.ACTION_DOWN)
		{
			if (stanje == PRIPRAVLJEN) {
				// At the beginning of the game, or the end of a previous one,
				// we should start a new game.	
				initNovaIgra();
				stanje = IGRA_TECE;
				osvezi();
				return (true);
			}else if(stanje == IZGUBIL)
				getParentActivity().finish();

			if ((y < slope*x) && (y < -slope*x + height)) { 
				if (smer != JUG) {
					naslednjaSmer = SEVER;
				}
				return (true);
			}
			// Touch event DOWN
			if ((y > slope*x) && (y > -slope*x + height)) { 
				if (smer != SEVER) {
					naslednjaSmer = JUG;
				}
				return (true);
			}

			// Touch event LEFT
			if ((y > slope*x) && (y < (-slope*x + height))) {
				if (smer != VZHOD) {
					naslednjaSmer = ZAHOD;
				}
				return (true);
			}

			// Touch event RIGHT
			if ((y < slope*x) && (y > -slope*x + height)) {
				if (smer != ZAHOD) {
					naslednjaSmer = VZHOD;
				}
				return (true);
			}
		}
		return false;
	}
	/*	@Override
	public void onOrientationChanged(float azimuth, float pitch, float roll) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTopUp() {
		// TODO Auto-generated method stub
		if (smer != JUG) {
			naslednjaSmer = SEVER;
		}
		Toast.makeText(CONTEXT, "UP", Toast.LENGTH_LONG).show();

	}

	@Override
	public void onBottomUp() {
		// TODO Auto-generated method stub
		Toast.makeText(CONTEXT, "bottmUP", Toast.LENGTH_LONG).show();
		if (smer != SEVER) {
			naslednjaSmer = JUG;
		}


	}

	@Override
	public void onRightUp() {
		// TODO Auto-generated method stub
		if (smer != VZHOD) {
			naslednjaSmer = ZAHOD;
		}
		Toast.makeText(CONTEXT, "DESNO", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onLeftUp() {
		// TODO Auto-generated method stub
		if (smer != ZAHOD) {
			naslednjaSmer = VZHOD;
		}
		Toast.makeText(CONTEXT, "LEVO", Toast.LENGTH_LONG).show();

	}*/
}
