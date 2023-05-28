package other;

public class Other {
	
	public static double dist(double x1, double y1, double x2, double y2) {
		double xd = x1 - x2;
		double yd = y1 - y2;
		return Math.sqrt(xd * xd + yd * yd);
	}
	
	public static String getText(int n) {
		switch(n) {
		case 1: return "Chapter 1\n\nIn ancient times, there was a mountain with a spirit stone on the top.\n "
				+ "One day, you born from this stone. However, God decided to suppress you at\n"
				+ " the foot of Wuzhi Mountain. Five hundred years later, Tang Monk passed \n"
				+ "through Wuzhi Mountain and rescued you.  Under God's guidance, you escorted\n"
				+ " Tang Monk all the way to distant India to obtain the holy scriptures.On the\n"
				+ " way, Flame Mountain blocked  your way, and in order to extinguish the fire,\n"
				+ " you have to defeat the Bull Demon King to obtain the Iron Fan Princess's fan.\n";
		case 2: return "Chapter 2\n\nYou can finally continue on your journey. However, you must be careful \n"
				+ "because Tang Monk's flesh is capable of granting immortality. Lady White Bone\n "
				+ "Demon is attempting to acquire Tang Monk's flesh, please protect Tang Mon\n";
		case 3: return "Chapter 3\n\nTang Monk accidentally entered the lair of seven spider demons while \n"
				+ "he was alone and was unable to escape as he got tangled up in their silk threads.\n"
				+ " Please use your bravery and ingenuity to rescue him!";
		default: return"";
		}
	}
}
