import { GuestDinnerViewDto, HostDinnerViewDto } from "@/types";

// A sample 1x1 transparent pixel Base64 encoded string
const sampleBase64Image =
  "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C8AAAAASUVORK5CYII=";

export const sampleGuestDinnerView: GuestDinnerViewDto = {
  preDinnerInfo: {
    uuid: "dinner-123",
    dateTime: "2025-07-01T19:00:00Z",
    host: {
      uuid: "user-1",
      name: "Alice Host",
      avatarUrl: sampleBase64Image,
    },
    storyTitle: "Murder at the Manor",
    storyBannerData: sampleBase64Image,
    setting: "A grand estate in the English countryside, 1920s.",
    rules: "No phones, stay in character, and have fun!",
    participants: [
      {
        user: {
          uuid: "user-2",
          name: "Bob Guest",
          avatarUrl: sampleBase64Image,
        },
        character: {
          uuid: "char-1",
          name: "Inspector Grey",
          age: 42,
          shopDescription: "A sharp detective with a keen eye for detail.",
          avatarData: sampleBase64Image,
          role: "Detective",
        },
      },
      {
        user: {
          uuid: "user-3",
          name: "Clara Suspect",
          avatarUrl: sampleBase64Image,
        },
        character: {
          uuid: "char-2",
          name: "Lady Blackwood",
          age: 35,
          shopDescription: "The enigmatic lady of the manor.",
          avatarData: sampleBase64Image,
          role: "Suspect",
        },
      },
    ],
  },
  privateInfo: {
    characterId: "char-1",
    characterDescription:
      "You are Inspector Grey, tasked with solving the murder.",
    relationships: {
      "char-2": "You suspect Lady Blackwood is hiding something.",
    },
    stages: [
      {
        stageTitle: "Arrival",
        objectiveDesc: "Introduce yourself and mingle.",
        events: [
          {
            time: "19:00",
            title: "Welcome Toast",
            description: "The host welcomes everyone to the manor.",
          },
        ],
      },
      {
        stageTitle: "Investigation",
        objectiveDesc: "Gather clues and question suspects.",
        events: [
          {
            time: "19:30",
            title: "Clue Discovery",
            description: "A mysterious letter is found in the library.",
          },
        ],
      },
    ],
  },
  conclusion: {
    voteOpen: true,
    criminalIds: ["char-2"],
    motive: "Jealousy over inheritance.",
    votes: [
      {
        guestId: "user-2",
        suspectIds: ["char-2"],
        motive: "She had the most to gain.",
      },
      {
        guestId: "user-3",
        suspectIds: ["char-1", "char-2"],
        motive: "They were too inquisitive.",
      },
    ],
  },
};

export const sampleHostDinnerView: HostDinnerViewDto = {
  preDinnerInfo: {
    uuid: "dinner-123",
    dateTime: "2025-07-01T19:00:00Z",
    host: {
      uuid: "user-1",
      name: "Alice Host",
      avatarUrl: sampleBase64Image,
    },
    storyTitle: "Murder at the Manor",
    storyBannerData: sampleBase64Image,
    setting: "A grand estate in the English countryside, 1920s.",
    rules: "No phones, stay in character, and have fun!",
    participants: [
      {
        user: {
          uuid: "user-2",
          name: "Bob Guest",
          avatarUrl: sampleBase64Image,
        },
        character: {
          uuid: "char-1",
          name: "Inspector Grey",
          age: 42,
          shopDescription: "A sharp detective with a keen eye for detail.",
          avatarData: sampleBase64Image,
          role: "Detective",
        },
      },
      {
        user: {
          uuid: "user-3",
          name: "Clara Suspect",
          avatarUrl: sampleBase64Image,
        },
        character: {
          uuid: "char-2",
          name: "Lady Blackwood",
          age: 35,
          shopDescription: "The enigmatic lady of the manor.",
          avatarData: sampleBase64Image,
          role: "Suspect",
        },
      },
    ],
  },
  privateInfo: {
    characterId: "char-1",
    characterDescription:
      "You are Inspector Grey, tasked with solving the murder.",
    relationships: {
      "char-2": "You suspect Lady Blackwood is hiding something.",
    },
    stages: [
      {
        stageTitle: "Arrival",
        objectiveDesc: "Introduce yourself and mingle.",
        events: [
          {
            time: "19:00",
            title: "Welcome Toast",
            description: "The host welcomes everyone to the manor.",
          },
        ],
      },
      {
        stageTitle: "Investigation",
        objectiveDesc: "Gather clues and question suspects.",
        events: [
          {
            time: "19:30",
            title: "Clue Discovery",
            description: "A mysterious letter is found in the library.",
          },
        ],
      },
    ],
  },
  conclusion: {
    voteOpen: false,
    criminalIds: ["char-2"],
    motive: "Jealousy over inheritance.",
    votes: [
      {
        guestId: "user-2",
        suspectIds: ["char-2"],
        motive: "She had the most to gain.",
      },
      {
        guestId: "user-3",
        suspectIds: ["char-1"],
        motive: "He was too inquisitive.",
      },
    ],
  },
  hostInfo: {
    briefing: "Ensure everyone stays in character and follows the rules.",
    assignments: [
      {
        characterId: "char-1",
        userId: "user-2",
        inviteCode: "INVITE-123-A",
      },
      {
        characterId: "char-2",
        userId: "user-3",
        inviteCode: "INVITE-123-B",
      },
    ],
    missingPrivateInfo: [],
    stagePrompts: [
      "Prompt players to share their clues at the end of each stage.",
    ],
    allHaveVoted: true,
  },
};
