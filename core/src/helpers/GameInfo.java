package helpers;

public class GameInfo {  // chứa thông tin trong game

    // chiều rộng và chiều cao của cửa sổ trò chơi
    public final static int Width = 480;
    public final static int Height = 800;
    public final static  int PPM = 100;  // tỷ lệ chuyển đổi pixel sang mét

    // Các hằng số DEFAULT, PLAYER, COLLECTABLE và DESTROYED đại diện cho các CategoryBit khác nhau được sử dụng để đánh dấu các Fixture trong game
    // Khi các Fixture va chạm với nhau, các vùng Category này được sử dụng để xác định cách thức xử lý va chạm giữa các đối tượng
    // Ví dụ, nếu một Fixture có CategoryBit là COLLECTABLE va chạm với một Fixture có CategoryBit là PLAYER,
    // thì đối tượng PLAYER sẽ được xử lý để thu thập đối tượng COLLECTABLE
    public static final short DEFAULT = 1;
    public static final short PLAYER = 2;
    public static final short COLLECTABLE = 4;
    public static final short DESTROYED = 6;

    // Các hằng số này có giá trị khác nhau để có thể phân biệt được các CategoryBit khác nhau và đảm bảo tính hiệu quả của xử lý va chạm
}
