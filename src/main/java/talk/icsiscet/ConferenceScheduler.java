package talk.icsiscet;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class ConferenceScheduler {
    public static void main(String[] args) {
        // Step 1: Create a new model
        Model model = new Model("Conference Scheduling");

        // Step 2: Define variables - talks are assigned time slots and rooms
        int numTalks = 4; // 5 won't have a schedule
        int numRooms = 3;
        int numTimeSlots = 4;  // Assume 4 available time slots

        // Define variables for rooms and time slots for each talk
        IntVar[] roomAssignments = new IntVar[numTalks];
        IntVar[] timeSlotAssignments = new IntVar[numTalks];

        // Each talk is assigned a room from 0 to numRooms-1
        for (int i = 0; i < numTalks; i++) {
            roomAssignments[i] = model.intVar("Room" + i, 0, numRooms - 1);
            timeSlotAssignments[i] = model.intVar("TimeSlot" + i, 0, numTimeSlots - 1);
        }

        // Step 3: Add constraints
        // 1. No two talks in the same room can overlap in time
        for (int i = 0; i < numTalks; i++) {
            for (int j = i + 1; j < numTalks; j++) {
                // If two talks are in the same room, they must be in different time slots
                model.ifThen(
                        model.arithm(roomAssignments[i], "=", roomAssignments[j]),
                        model.arithm(timeSlotAssignments[i], "!=", timeSlotAssignments[j])
                );
            }
        }

        // 2. Add buffer time constraints for attendees to switch between rooms
        // For simplicity, ensure talks in different rooms do not start back-to-back
        for (int i = 0; i < numTalks; i++) {
            for (int j = i + 1; j < numTalks; j++) {
                model.ifThen(
                        model.arithm(roomAssignments[i], "!=", roomAssignments[j]),
                        model.distance(timeSlotAssignments[i], timeSlotAssignments[j], ">", 0)  // Ensure at least 1-time slot difference
                );
            }
        }

        // Step 4: Solve the model
        Solver solver = model.getSolver();
        if (solver.solve()) {
            for (int i = 0; i < numTalks; i++) {
                System.out.println("Talk " + i + " assigned to Room " + roomAssignments[i].getValue() +
                        " at TimeSlot " + timeSlotAssignments[i].getValue());
            }
        } else {
            System.out.println("No valid schedule found!");
        }
    }
}