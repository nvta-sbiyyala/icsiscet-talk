package talk.icsiscet;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

import java.util.stream.IntStream;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class ConferenceScheduler {
    public static void main(String[] args) {
        // Create a new model
        Model model = new Model("Conference Scheduling");

        // Define variables - talks are assigned time slots and rooms
        int numTalks = 4; // 5 won't have a schedule
        int numRooms = 3;
        int numSlots = 4;  // Assume 4 available time slots

        // Define variables for rooms and time slots for each talk
        IntVar[] roomAssignments = new IntVar[numTalks];
        IntVar[] slotAssignments = new IntVar[numTalks];

        // Each talk is assigned a room from 0 to numRooms-1
        IntStream.range(0, numTalks).forEachOrdered(i -> {
            roomAssignments[i] = model.intVar("Room" + i, 0, numRooms - 1);
            slotAssignments[i] = model.intVar("TimeSlot" + i, 0, numSlots - 1);
        });

        // Step 3: Add constraints using IntStream

        // 1. No two talks in the same room can overlap in time
        IntStream.range(0, numTalks).forEach(i ->
                IntStream.range(i + 1, numTalks).forEach(j ->
                        model.ifThen(
                                model.arithm(roomAssignments[i], "=", roomAssignments[j]),
                                model.arithm(slotAssignments[i], "!=", slotAssignments[j])
                        )
                )
        );

        // 2. Add buffer time constraints for attendees to switch between rooms
        IntStream.range(0, numTalks).forEach(i ->
                IntStream.range(i + 1, numTalks).forEach(j ->
                        model.ifThen(
                                model.arithm(roomAssignments[i], "!=", roomAssignments[j]),
                                // Ensure at least 1-time slot difference
                                model.distance(slotAssignments[i], slotAssignments[j], ">", 0)
                        )
                )
        );

        // Step 4: Solve the model
        Solver solver = model.getSolver();
        if (solver.solve()) {
            IntStream.range(0, numTalks)
                    .forEach(i -> System.out.println("Talk " + i + " assigned to Room "
                            + roomAssignments[i].getValue() + " at TimeSlot "
                            + slotAssignments[i].getValue()));
        } else {
            System.out.println("No valid schedule found!");
        }
    }
}