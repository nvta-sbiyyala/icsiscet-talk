package talk.icsiscet;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

import java.util.stream.IntStream;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class ConferenceScheduler {
    public static void main(String[] args) {
        // Step 1: Create a new model
        Model model = new Model("Conference Scheduling");

        // Step 2: Define variables - talks are assigned time slots and rooms
        int numTalks = 12;
        int numRooms = 9;
        int numSlots = 12;

        // Define variables for rooms and time slots for each talk
        IntVar[] roomAssigns = new IntVar[numTalks];
        IntVar[] slotAssigns = new IntVar[numTalks];

        // Each talk is assigned a room from 0 to numRooms-1
        IntStream.range(0, numTalks).forEachOrdered(i -> {
            roomAssigns[i] = model.intVar("Room" + i, 0, numRooms - 1);
            slotAssigns[i] = model.intVar("TimeSlot" + i, 0, numSlots - 1);
        });

        // Step 3: Add constraints using IntStream
        // 1. No two talks in the same room can overlap in time
        IntStream.range(0, numTalks).forEach(i ->
                IntStream.range(i + 1, numTalks).forEach(j ->
                        model.ifThen(
                                model.arithm(roomAssigns[i], "=", roomAssigns[j]),
                                model.arithm(slotAssigns[i], "!=", slotAssigns[j])
                        )
                )
        );

        // 2. Add buffer time constraints for attendees to switch between rooms
        IntStream.range(0, numTalks).forEach(i ->
                IntStream.range(i + 1, numTalks).forEach(j ->
                        model.ifThen(
                                model.arithm(roomAssigns[i], "!=", roomAssigns[j]),
                                // Ensure at least 1-time slot difference
                                model.distance(slotAssigns[i], slotAssigns[j], ">", 0)
                        )
                )
        );

        // Step 4: Solve the model
        Solver solver = model.getSolver();
        if (solver.solve()) {
            IntStream.range(0, numTalks)
                    .forEach(i -> System.out.println("Talk " + i + " assigned to Room "
                            + roomAssigns[i].getValue() + " at TimeSlot "
                            + slotAssigns[i].getValue()));
        } else {
            System.out.println("No valid schedule found!");
        }
    }
}