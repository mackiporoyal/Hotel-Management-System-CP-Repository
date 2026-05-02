package finals;

public class DisplayRoom {
	RoomAvailability.Floors[] floors = RoomAvailability.Floors.values();
	private char[][] rooms ={
			{'A','A','A','A','A','A','A','A',},
			{'■','■','■','■','■','■','■','■',},
			{'■','■','■','■','■','■','■','■',},
			{'A','A','A','A','A','A','A','A',},
			};
	
	int floorNum;
	public void floor(int floorNum) {
		this.floorNum = floorNum;
	}
	public void DisplayRoom() {
	rooms[0][2] = 'X';
	System.out.println("\n\t\t\t\t===================================================");
	System.out.println("\t\t\t\t_________________Room Availability_________________");
	
	for (int z = 0; z<4; z++) {
		System.out.print("\t\t");
		for(int x = 0; x<8; x++){
			System.out.print("\t    ["+ rooms[z][x]+"]");
		}
		System.out.println();
	}
	}
}
