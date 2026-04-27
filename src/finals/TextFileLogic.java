package finals;

import java.nio.file.*;


public class TextFileLogic {

	Path storagePath = Paths.get("HotelDatabase.txt");
	Path fullPath = storagePath.toAbsolutePath();
	public void d() {
		System.out.println(this.fullPath);
	}
}
