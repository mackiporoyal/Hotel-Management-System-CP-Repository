package finals;

public class RoomAvailability {
	public enum Floors {
		FIRST, SECOND, THIRD, FOURTH, FIFTH;
	}
	
	private char[][] rooms ={
					{'A','A','A','A','A','A','A','A',},
					{'■','■','■','■','■','■','■','■',},
					{'■','■','■','■','■','■','■','■',},
					{'A','A','A','A','A','A','A','A',},
					};
	
	public void displayRoom() {
		System.out.println("\n\t\t\t\t==================================================");
		System.out.println("\t\t\t\t_________________Room Availability________________");
		
		for (int z = 0; z<4; z++) {
			for(int x = 0; x<8; x++){
				System.out.print("\t");
				System.out.print("\t"+ rooms[z][x]);
			}
			System.out.println();
		}
	}
}
