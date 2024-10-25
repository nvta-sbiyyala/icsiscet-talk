package talk.icsiscet;

import java.util.*;

// Constraint Propagation Implementation: Using propagation to reduce conflicts
class ConstraintPropagationConferenceScheduler {
    static class Talk {
        String name;
        int start;
        int end;

        Talk(String name, int start, int end) {
            this.name = name;
            this.start = start;
            this.end = end;
        }
    }

    public static void main(String[] args) {
        // Setup: Start
        Random random = new Random();
        List<Talk> talks = new ArrayList<>();
        for (int i = 0; i < 50000; i++) {
            int start = random.nextInt(24);
            int end = start + 1 + random.nextInt(3); // End time is 1 to 3 hours after start
            talks.add(new Talk("Talk " + (i + 1), start, Math.min(end, 24)));
        }
        // Setup: End

        long startTime = System.nanoTime();

        // Sort talks by their start times to apply constraint propagation more effectively
        talks.sort(Comparator.comparingInt(t -> t.start));

        int roomCount = 0;
        List<List<Talk>> rooms = new ArrayList<>();

        for (Talk talk : talks) {
            boolean scheduled = false;
            for (List<Talk> room : rooms) {
                Talk lastTalkInRoom = room.get(room.size() - 1);
                if (lastTalkInRoom.end <= talk.start) {
                    room.add(talk);
                    scheduled = true;
                    break;
                }
            }
            if (!scheduled) {
                List<Talk> newRoom = new ArrayList<>();
                newRoom.add(talk);
                rooms.add(newRoom);
                roomCount++;
            }
        }

        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000; // Convert to milliseconds

        System.out.println("Constraint Propagation approach - Number of rooms required: " + roomCount);
        System.out.println("Constraint Propagation approach - Time taken: " + duration + " ms");
    }
}
