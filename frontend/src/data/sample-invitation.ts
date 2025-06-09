import { InvitationViewDto } from "@/types";

export const sampleInvitation: InvitationViewDto = {
  inviteCode: "DINNER2025",
  dateTime: "2025-07-01T19:00:00Z",
  host: {
    uuid: "host-uuid-1",
    name: "Alice Host",
    avatarUrl: "https://randomuser.me/api/portraits/women/8.jpg",
  },
  storyTitle: "A Mysterious Evening",
  storyBannerUrl:
    "https://images.unsplash.com/photo-1506744038136-46273834b3fb",
  dinnerStoryBrief:
    "Join us for a night of intrigue, secrets, and delicious food as you step into your character's shoes.",
  yourAssignedCharacter: {
    uuid: "char-uuid-1",
    name: "Detective Rowan",
    shopDescription:
      "A sharp-minded detective with a knack for solving the unsolvable.",
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
        shopDescription:
          "A world-renowned chef with a secret recipe and a mysterious past.",
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
        shopDescription:
          "An elegant heiress with a fortune—and a few secrets of her own.",
      },
    },
    // Not yet RSVP'd: Detective Rowan (the invitation is for this character)
    {
      user: {
        uuid: "",
        name: "",
        avatarUrl: "",
      },
      character: {
        uuid: "char-uuid-1",
        name: "Detective Rowan",
        shopDescription: "A sharp-minded detective with a knack for solving the unsolvable.",
      },
    },
    // Not yet RSVP'd: Professor Sterling
    {
      user: {
        uuid: "",
        name: "",
        avatarUrl: "",
      },
      character: {
        uuid: "char-uuid-4",
        name: "Professor Sterling",
        shopDescription: "A brilliant professor whose knowledge may hold the key to the evening's mystery.",
      },
    },
  ],
  canAccept: true,
};
