import { InvitationViewDto } from "@/types";

// A sample 1x1 transparent pixel Base64 encoded string
const sampleBase64Image = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C8AAAAASUVORK5CYII=";

export const sampleInvitation: InvitationViewDto = {
  inviteCode: "DINNER2025",
  dateTime: "2025-07-01T19:00:00Z",
  host: {
    uuid: "host-uuid-1",
    name: "Alice Host",
    avatarUrl: "https://randomuser.me/api/portraits/women/8.jpg",
  },
  storyTitle: "A Mysterious Evening",
  storyBannerData: sampleBase64Image,
  dinnerStoryBrief:
    "Join us for a night of intrigue, secrets, and delicious food as you step into your character's shoes.\n\nThe evening will be filled with unexpected twists, hidden motives, and opportunities to uncover the truth.\n\nWill you solve the mystery, or become part of it?",
  yourAssignedCharacter: {
    uuid: "char-uuid-1",
    name: "Detective Rowan",
    role: "Detective",
    shopDescription:
      "A sharp-minded detective with a knack for solving the unsolvable.",
    avatarData: sampleBase64Image,
  },
  otherParticipants: [
    {
      user: {
        uuid: "guest-uuid-2",
        name: "Bob Guest",
        avatarUrl: "https://randomuser.me/api/portraits/men/2.jpg",
      },
      character: {
        uuid: "char-uuid-2",
        name: "Chef Lucien",
        role: "Chef",
        shopDescription:
          "A world-renowned chef with a secret recipe and a mysterious past.",
        avatarData: sampleBase64Image,
      },
    },
    {
      user: {
        uuid: "guest-uuid-3",
        name: "Carol Guest",
        avatarUrl: "https://randomuser.me/api/portraits/women/3.jpg",
      },
      character: {
        uuid: "char-uuid-3",
        name: "Heiress Vivienne",
        role: "Heiress",
        shopDescription:
          "An elegant heiress with a fortune—and a few secrets of her own.",
        avatarData: sampleBase64Image,
      },
    },
    // Not yet RSVP'd: Detective Rowan (the invitation is for this character)
    {
      user: undefined,
      character: {
        uuid: "char-uuid-1",
        name: "Detective Rowan",
        role: "Detective",
        shopDescription:
          "A sharp-minded detective with a knack for solving the unsolvable.",
        avatarData: sampleBase64Image,
      },
    },
    // Not yet RSVP'd: Professor Sterling
    {
      user: undefined,
      character: {
        uuid: "char-uuid-4",
        name: "Professor Sterling",
        role: "Professor",
        shopDescription:
          "A brilliant professor whose knowledge may hold the key to the evening's mystery.",
        avatarData: sampleBase64Image,
      },
    },
  ],
  canAccept: true,
};
