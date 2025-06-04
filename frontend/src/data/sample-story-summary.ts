import type { StorySummary } from "@/types";

export const sampleStorySummary: StorySummary = {
  id: "1a2b3c4d-5e6f-7a8b-9c0d-1e2f3a4b5c6d",
  storyId: "1a2b3c4d-5e6f-7a8b-9c0d-1e2f3a4b5c6d",
  title: "The Haunted Manor",
  thumbnailDescription: "A chilling mystery in an old, abandoned mansion.",
  minPlayers: 4,
  maxPlayers: 6,
  characters: [
    {
      id: "char-1",
      name: "Inspector Holmes",
      characterDescription: "A sharp-minded detective with a mysterious past.",
      gender: "MALE",
    },
    {
      id: "char-2",
      name: "Lady Violet",
      characterDescription: "An elegant woman with a secret agenda.",
      gender: "FEMALE",
    },
    {
      id: "char-3",
      name: "Professor Blackwood",
      characterDescription: "A reclusive scholar obsessed with the occult.",
      gender: "MALE",
    },
    {
      id: "char-4",
      name: "Miss Rose",
      characterDescription: "A young maid who knows more than she lets on.",
      gender: "FEMALE",
    },
    {
      id: "char-5",
      name: "Dr. Green",
      characterDescription: "A mysterious doctor with ambiguous motives.",
      gender: "OTHER",
    },
    {
      id: "char-6",
      name: "Colonel Mustard",
      characterDescription: "A retired military man with a temper.",
      gender: "MALE",
    },
  ],
  configs: [
    {
      id: "cfg-1234",
      playerCount: 4,
      genderCounts: { MALE: 2, FEMALE: 2 },
      characterIds: ["char-1", "char-2", "char-3", "char-4"],
    },
    {
      id: "cfg-5678",
      playerCount: 6,
      genderCounts: { MALE: 3, FEMALE: 2, OTHER: 1 },
      characterIds: [
        "char-1",
        "char-2",
        "char-3",
        "char-4",
        "char-5",
        "char-6",
      ],
    },
  ],
};
