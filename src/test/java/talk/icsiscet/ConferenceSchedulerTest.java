package talk.icsiscet;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConferenceSchedulerTest {

    @Test
    public void testValidSchedule() {
        Model model = new Model("Conference Scheduling");

        int numTalks = 4;
        int numRooms = 3;
        int numSlots = 4;

        IntVar[] roomAssignments = new IntVar[numTalks];
        IntVar[] slotAssignments = new IntVar[numTalks];

        // Define room and slot variables
        IntStream.range(0, numTalks).forEachOrdered(i -> {
            roomAssignments[i] = model.intVar("Room" + i, 0, numRooms - 1);
            slotAssignments[i] = model.intVar("TimeSlot" + i, 0, numSlots - 1);
        });

        // Add constraints: No two talks in the same room can overlap in time
        IntStream.range(0, numTalks).forEach(i ->
                IntStream.range(i + 1, numTalks).forEach(j ->
                        model.ifThen(
                                model.arithm(roomAssignments[i], "=", roomAssignments[j]),
                                model.arithm(slotAssignments[i], "!=", slotAssignments[j])
                        )
                )
        );

        // Add buffer time constraints for switching between rooms
        IntStream.range(0, numTalks).forEach(i ->
                IntStream.range(i + 1, numTalks).forEach(j ->
                        model.ifThen(
                                model.arithm(roomAssignments[i], "!=", roomAssignments[j]),
                                model.distance(slotAssignments[i], slotAssignments[j], ">", 0)
                        )
                )
        );

        // Solve the model
        Solver solver = model.getSolver();
        assertTrue(solver.solve(), "The schedule should be solvable.");

        // Check that assignments are valid
        IntStream.range(0, numTalks).forEach(i -> {
            int room = roomAssignments[i].getValue();
            int slot = slotAssignments[i].getValue();
            System.out.println("Talk " + i + " assigned to Room " + room + " at TimeSlot " + slot);
            assertTrue(room >= 0 && room < numRooms, "Room assignment should be valid.");
            assertTrue(slot >= 0 && slot < numSlots, "Time slot assignment should be valid.");
        });
    }

    @Test
    public void testUnsolvableSchedule() {
        Model model = new Model("Conference Scheduling");

        int numTalks = 5; // Five talks, but only 4 time slots, making it unsolvable
        int numRooms = 3;
        int numSlots = 4;

        IntVar[] roomAssignments = new IntVar[numTalks];
        IntVar[] slotAssignments = new IntVar[numTalks];

        // Define room and slot variables
        IntStream.range(0, numTalks).forEachOrdered(i -> {
            roomAssignments[i] = model.intVar("Room" + i, 0, numRooms - 1);
            slotAssignments[i] = model.intVar("TimeSlot" + i, 0, numSlots - 1);
        });

        // Add constraints: No two talks in the same room can overlap in time
        IntStream.range(0, numTalks).forEach(i ->
                IntStream.range(i + 1, numTalks).forEach(j ->
                        model.ifThen(
                                model.arithm(roomAssignments[i], "=", roomAssignments[j]),
                                model.arithm(slotAssignments[i], "!=", slotAssignments[j])
                        )
                )
        );

        // Add buffer time constraints for switching between rooms
        IntStream.range(0, numTalks).forEach(i ->
                IntStream.range(i + 1, numTalks).forEach(j ->
                        model.ifThen(
                                model.arithm(roomAssignments[i], "!=", roomAssignments[j]),
                                model.distance(slotAssignments[i], slotAssignments[j], ">", 0)
                        )
                )
        );

        // Solve the model
        Solver solver = model.getSolver();
        assertFalse(solver.solve(), "The schedule should be unsolvable.");
    }

    @Test
    public void testRoomOverlapConstraint() {
        Model model = new Model("Conference Scheduling");

        int numTalks = 3;
        int numRooms = 2;
        int numSlots = 3;

        IntVar[] roomAssignments = new IntVar[numTalks];
        IntVar[] slotAssignments = new IntVar[numTalks];

        // Define room and slot variables
        IntStream.range(0, numTalks).forEachOrdered(i -> {
            roomAssignments[i] = model.intVar("Room" + i, 0, numRooms - 1);
            slotAssignments[i] = model.intVar("TimeSlot" + i, 0, numSlots - 1);
        });

        // Add constraints: No two talks in the same room can overlap in time
        IntStream.range(0, numTalks).forEach(i ->
                IntStream.range(i + 1, numTalks).forEach(j ->
                        model.ifThen(
                                model.arithm(roomAssignments[i], "=", roomAssignments[j]),
                                model.arithm(slotAssignments[i], "!=", slotAssignments[j])
                        )
                )
        );

        Solver solver = model.getSolver();
        assertTrue(solver.solve(), "The schedule should be solvable.");

        // Ensure no overlap in the same room at the same time slot
        for (int i = 0; i < numTalks; i++) {
            for (int j = i + 1; j < numTalks; j++) {
                if (roomAssignments[i].getValue() == roomAssignments[j].getValue()) {
                    assertTrue(slotAssignments[i].getValue() != slotAssignments[j].getValue(),
                            "Talks in the same room should have different time slots.");
                }
            }
        }
    }
}
