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
    "https://images.unsplash.com/photo-1736156725121-027231636f9d",
  dinnerStoryBrief:
    "Join us for a night of intrigue, secrets, and delicious food as you step into your character's shoes.\n\nThe evening will be filled with unexpected twists, hidden motives, and opportunities to uncover the truth.\n\nWill you solve the mystery, or become part of it?",
  yourAssignedCharacter: {
    uuid: "char-uuid-1",
    name: "Detective Rowan",
    role: "Detective",
    shopDescription:
      "A sharp-minded detective with a knack for solving the unsolvable.",
    avatarUrl: "https://randomuser.me/api/portraits/men/8.jpg",
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
        avatarUrl: "https://randomuser.me/api/portraits/men/10.jpg",
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
        avatarUrl: "https://randomuser.me/api/portraits/women/12.jpg",
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
        avatarUrl: "https://randomuser.me/api/portraits/men/8.jpg",
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
        avatarUrl: "https://randomuser.me/api/portraits/men/11.jpg",
      },
    },
  ],
  canAccept: true,
};
