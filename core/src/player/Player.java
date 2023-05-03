package player;   // dieu khien hanh dong va ve nhan vat

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import helpers.GameInfo;

public class Player extends Sprite {

    private World world;
    private Body body;
    private TextureAtlas playerAtlas;  // dung de load hinh anh cua nhan vat, duoc luu ruc duoi dang atlas
    private Animation<TextureRegion> animation;  // chua hanh dong cua nhan vat, duoc tao ra tu playerAtlas
    private float elapsedTime;  // thoi gian troi qua tu khi animation bat dau
    private boolean isWalking, dead;  // dai dien cho trang thai di bo va chet cua nhan vat

    // Ham khoi tao
    public Player(World world, float x, float y){
        super(new Texture("Player/Player 1.png"));
        this.world = world;
        setPosition(x, y);
        createBody();
        playerAtlas = new TextureAtlas("Player Animation/Player Animation.atlas");
        dead = false;
    }

    // tao vat the, dat vi tri ban dau va thiet lap huong cua nhan vat
    void createBody(){
        // dinh nghia doi tuong BodyDef de xac dinh cac thuoc tinh cua vat the, bao gom loại vat the (tinh hay dong), vi tri ban dau
        // van toc, huong, goc quay va khoi luong
        BodyDef bodyDef = new BodyDef();

        bodyDef.type = BodyDef.BodyType.DynamicBody;  // o day loai cua vat the ta de o dang dong
        // dat vi tri ban dau cua doi tuong cua vat the duoc tinh toan dua tren toa do cua doi tuong Player / hằng số PPM, r chuyen sang met
        bodyDef.position.set(getX() / GameInfo.PPM, getY() / GameInfo.PPM);

        // tao doi tuong body bang phuong thuc world.createBody(bodyDef)
        body = world.createBody(bodyDef);
        // dat thuoc tinh FixedRotation cua doi tuong body thanh true, de biet rang doi tuong khong the quay quanh truc cua no
        body.setFixedRotation(true);

        // tao hinh dang cua vat the bang cach sd PolygonShape của Box2D
        PolygonShape shape = new PolygonShape();
        // Ta tao hinh dang HCN bang phuong thuc setAsBox()
        shape.setAsBox((getWidth() / 2f - 20f) / GameInfo.PPM,
                (getHeight() / 2f) / GameInfo.PPM);

        // tao doi tuong FixtureDef de xac dinh cac thuoc tinh cua vat the bao gom KL, ma sat va hinh dang. Ngoai ra, ta con dat cac thuoc
        // tinh cho bo loc va cham (filter) bao gom categoryBits (phan loai vat the) và maskBits ( loai vat the ma co the va cham)
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 0f; // dat khoi luong cua vat the = 0 (khong co KL)
        fixtureDef.friction = 2f; // ma sat = 2 (ngan vat the truot tren cac be mat)
        fixtureDef.shape = shape; // hinh dang la HCN da tao ra o buoc truoc
        fixtureDef.filter.categoryBits = GameInfo.PLAYER; // dat bo loc va cham cho vat the la PLAYER (phan loai vat the cua nguoi choi)
        fixtureDef.filter.maskBits = GameInfo.DEFAULT | GameInfo.COLLECTABLE; // co the va cham voi cac vat the la mac dinh hoac COLLECTABLE
                                                                              // nghĩa là su va cham co the thu nhap duoc
        // tao doi tuong Fixture bang cach goi phuong thuc createFixture voi tham so fixtureDef
        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData("Player");   // dat thong tin nguoei dung cho fixture nay la Player

        shape.dispose();  // giai phong tai nguyen
    }

    //nhiem vu xu ly viec di chuyen cua nhan vat, cap nhat trang thai va thay doi huong quay cua hinh anh theo huong di chuyen
    public void movePlayer(float x){  // x: bieu thi cho van toc theo phuong ngang
        if(x < 0 && !this.isFlipX()) {
            // neu x < 0 va nhan vat dang huong ben phai, ham se lat hinh anh nhan vat theo phuong ngang de huong ve ben trai
            this.flip(true, false);
        } else if(x > 0 && this.isFlipX()) {
            // neu x < 0 va nhan vat dang huong ben trai, ham se lat hinh anh nhan vat theo phuong ngang de huong ve ben phai
            this.flip(true, false);
        }

        isWalking = true;  // danh dau nhan vat danng di chuyen
        // thiet lap van toc cho vat the cua nhan vat, giup di chuyen nhan vat theo huong mong muon
        body.setLinearVelocity(x, body.getLinearVelocity().y);
    }

    // phuong thuc ve nhan vat o trang thai dung yen
    public void drawPlayerIdle(SpriteBatch batch){
        // neu nhan vat khong di chuyen ( bien isWalking = false), ham se ve hinh anh nhan vat tai vi tri hien tai tren man hinh
        if(!isWalking){
            batch.draw(this, getX() + getWidth() / 2f - 20,
                    getY() - getHeight() / 2f);
        }
    }

    // phuong thuc ve nhan vat trong trang thai di chuyen, sd animation de tao hieu ung chuyen dong
    public void drawPlayerAnimation(SpriteBatch batch) { // nhap vao tham so batch de ve nhan vat khi di chuyen
        // neu nhan vat dang di chuyen , ham se ve hinh anh nhan vat theo 1 chuoi cac hinh anh tao thanh 1 animation duoc lay tu TextureAtlas
        if(isWalking) {
            elapsedTime += Gdx.graphics.getDeltaTime();  // tinh toan thoi gín troi qua

            // lay danh sach cac khung hinh từ atlas va lap qua tung khung hinh. Neu nhan vat dang di chuyen ve ben trai va khung hinh
            // chua duoc lat nguoc, phuong thuc nay se lat nguoc khung hinh de nhan vat quay mat ve ben trai va nguoc lai
            Array<TextureAtlas.AtlasRegion> frames = playerAtlas.getRegions();
            for(TextureRegion frame : frames) {
                if(body.getLinearVelocity().x < 0 && !frame.isFlipX()) {
                    frame.flip(true, false);
                } else if(body.getLinearVelocity().x > 0 && frame.isFlipX()) {
                    frame.flip(true, false);
                }
            }

            // tao ra doi tuong aimation de chua cac khung hinh da lat nguoc, tham so dau tien cua doi tuong la toc do di chuyen cua
            // hinh, tinh bang cach chia thoi gian can thay doi giua cac khung hinh (= 1/10 s). tsao thuu 2 la danh sach cac khung hinh
            // da lat nguoc
            animation = new Animation(1f/10f, playerAtlas.getRegions());

            // ve nhan vat voi khung hinh hien tai cua doi tuong animation
            batch.draw(animation.getKeyFrame(elapsedTime, true), // khung hinh hỉen tai lay tu animation
                    getX() + getWidth() / 2f - 20f, // vi tri de ve nhan vat tren man hinh
                    getY() - getHeight() / 2f);
        }
    }

    // cập nhật vị trí của nhân vật trên màn hình, kiểm tra xem nhân vật có đang di chuyển sang trái hoặc
    // sang phải không dựa trên vận tốc tuyến tính của cơ thể (body.getLinearVelocity().x).
    public void updatePlayer(){
         // Nếu nhân vật đang di chuyển sang phải, vị trí của nhân vật được đặt tại vị trí hiện tại của cơ thể
        // nhân với tỷ lệ pixel trên mét (GameInfo.PPM), giá trị này được đặt cho vị trí x của nhân vật trên màn hình
        if(body.getLinearVelocity().x > 0){
            setPosition(body.getPosition().x * GameInfo.PPM,
                    body.getPosition().y * GameInfo.PPM);
        } else if(body.getLinearVelocity().x < 0){
            setPosition((body.getPosition().x - 0.3f) * GameInfo.PPM,
                    body.getPosition().y * GameInfo.PPM);
        }
    }


    // thiết lập và truy xuất các trạng thái của nhân vật.

    public void setWalking(boolean isWalking){
        this.isWalking = isWalking;
    }
    public void setDead(boolean dead) {
        this.dead = dead;
    }
    public boolean isDead(){
        return dead;
    }
}
