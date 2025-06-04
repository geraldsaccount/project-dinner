import type { StorySummary } from "@/types";

export const sampleStorySummary: StorySummary = {
  id: "1a2b3c4d-5e6f-7a8b-9c0d-1e2f3a4b5c6d",
  // storyId: "1a2b3c4d-5e6f-7a8b-9c0d-1e2f3a4b5c6d",
  title: "The Haunted Manor",
  thumbnailDescription: "A chilling mystery in an old, abandoned mansion.",
  minPlayers: 4,
  maxPlayers: 6,
  characters: [
    {
      id: "820c5840-13f8-4905-9638-ab8267f3c619",
      name: "Inspector Holmes",
      characterDescription: "A sharp-minded detective with a mysterious past.",
      gender: "MALE",
    },
    {
      id: "4c4641f2-5a33-4cc0-a800-464089786b3e",
      name: "Lady Violet",
      characterDescription: "An elegant woman with a secret agenda.",
      gender: "FEMALE",
    },
    {
      id: "a278d3ca-9a70-4b5b-a0b7-8b4342d8dd24",
      name: "Professor Blackwood",
      characterDescription: "A reclusive scholar obsessed with the occult.",
      gender: "MALE",
    },
    {
      id: "ea6cfc0e-d5c5-43da-8151-39bf2d1a9e31",
      name: "Miss Rose",
      characterDescription: "A young maid who knows more than she lets on.",
      gender: "FEMALE",
    },
    {
      id: "1095ff84-4d02-429d-b0b9-4c1fb0006549",
      name: "Dr. Green",
      characterDescription: "A mysterious doctor with ambiguous motives.",
      gender: "OTHER",
    },
    {
      id: "cc5bc97c-d8b2-48ae-b16e-314823b9aa43",
      name: "Colonel Mustard",
      characterDescription: "A retired military man with a temper.",
      gender: "MALE",
    },
  ],
  configs: [
    {
      id: "3585c1eb-421a-4872-97fa-9af7ef40948d",
      playerCount: 4,
      genderCounts: { MALE: 2, FEMALE: 2 },
      characterIds: [
        "820c5840-13f8-4905-9638-ab8267f3c619",
        "4c4641f2-5a33-4cc0-a800-464089786b3e",
        "a278d3ca-9a70-4b5b-a0b7-8b4342d8dd24",
        "1095ff84-4d02-429d-b0b9-4c1fb0006549",
      ],
    },
    {
      id: "a91527c4-de93-416a-b56f-2e3756c4abfa",
      playerCount: 6,
      genderCounts: { MALE: 3, FEMALE: 2, OTHER: 1 },
      characterIds: [
        "820c5840-13f8-4905-9638-ab8267f3c619",
        "4c4641f2-5a33-4cc0-a800-464089786b3e",
        "a278d3ca-9a70-4b5b-a0b7-8b4342d8dd24",
        "ea6cfc0e-d5c5-43da-8151-39bf2d1a9e31",
        "1095ff84-4d02-429d-b0b9-4c1fb0006549",
        "cc5bc97c-d8b2-48ae-b16e-314823b9aa43",
      ],
    },
  ],
};
