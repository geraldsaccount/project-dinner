import { GuestDinnerViewDto, HostDinnerViewDto } from "@/types";

export const sampleGuestDinnerView: GuestDinnerViewDto = {
  uuid: "dinner-uuid-1",
  dateTime: "2025-07-01T19:00:00Z",
  host: {
    uuid: "host-uuid-1",
    name: "Alice Host",
    avatarUrl: "https://randomuser.me/api/portraits/women/8.jpg",
  },
  storyTitle: "Murder at the Manor",
  storyBannerUrl:
    "https://images.unsplash.com/photo-1506744038136-46273834b3fb",
  dinnerStoryBrief:
    "Welcome to Blackwood Manor, a sprawling estate steeped in history and shrouded in mystery.\n\nTonight, the manor is alive with the clinking of glasses and the hum of conversation as distinguished guests gather for an exclusive dinner party hosted by the enigmatic Lady Eleanor Blackwood. The grand halls are adorned with priceless art, and the air is thick with secrets—old rivalries, hidden romances, and long-buried scandals.\n\nBut as the evening unfolds, tragedy strikes: the host is found dead, and the guests quickly realize that no one can be trusted. Each of you has a role to play in uncovering the truth, but beware—everyone has something to hide, and the walls of Blackwood Manor have ears.",
  participants: [
    {
      user: {
        uuid: "guest-uuid-2",
        name: "Bob Guest",
        avatarUrl: "https://randomuser.me/api/portraits/men/2.jpg",
      },
      character: {
        uuid: "char-uuid-2",
        name: "Eleanor Blackwood",
        role: "Hostess",
        shopDescription:
          "The enigmatic lady of the manor, Eleanor's calm hides a lifetime of secrets and intrigue.",
        avatarUrl: "https://randomuser.me/api/portraits/women/4.jpg",
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
        name: "Reginald Mustard",
        role: "Retired Colonel",
        shopDescription:
          "A retired military man, Reginald's stern exterior and cryptic stories make him respected and feared.",
        avatarUrl: "https://randomuser.me/api/portraits/men/5.jpg",
      },
    },
    {
      user: {
        uuid: "host-uuid-1",
        name: "Alice Host",
        avatarUrl: "https://randomuser.me/api/portraits/women/8.jpg",
      },
      character: {
        uuid: "char-uuid-1",
        name: "Arthur Doyle",
        role: "Detective",
        shopDescription:
          "A sharp detective, Arthur is known for solving the most perplexing cases and uncovering the manor's secrets.",
        avatarUrl: "https://randomuser.me/api/portraits/men/6.jpg",
      },
    },
    {
      user: undefined,
      character: {
        uuid: "char-uuid-5",
        name: "Dr. Felix Marrow",
        role: "Forensic Scientist",
        shopDescription:
          "A meticulous forensic scientist whose expertise in uncovering hidden evidence is matched only by his own mysterious past.",
        avatarUrl: "https://randomuser.me/api/portraits/men/15.jpg",
      },
    },
  ],
  yourPrivateInfo: {
    characterId: "char-uuid-1",
    privateDescription:
      "You are Arthur Doyle, a renowned detective invited to Blackwood Manor under the guise of a social visit.\n\nSecretly, you were contacted by an anonymous source who hinted at a crime about to take place. Your motivation is to solve the murder and restore your reputation after a recent case went unsolved.\n\nUnknown to the others, you once had a secret affair with Lady Eleanor Blackwood, and you must keep this hidden to avoid scandal and suspicion.\n\nYour keen observation skills and logical mind are your greatest assets, but trust no one—any guest could be the killer, and your past connection to the host could make you a target.",
  },
};

export const sampleHostDinnerView: HostDinnerViewDto = {
  uuid: "dinner-uuid-1",
  dateTime: "2025-07-01T19:00:00Z",
  host: {
    uuid: "host-uuid-1",
    name: "Alice Host",
    avatarUrl: "https://randomuser.me/api/portraits/women/8.jpg",
  },
  storyTitle: "Murder at the Manor",
  storyBannerUrl:
    "https://images.unsplash.com/photo-1506744038136-46273834b3fb",
  dinnerStoryBrief:
    "Welcome to Blackwood Manor, a sprawling estate steeped in history and shrouded in mystery.\n\nTonight, the manor is alive with the clinking of glasses and the hum of conversation as distinguished guests gather for an exclusive dinner party hosted by the enigmatic Lady Eleanor Blackwood. The grand halls are adorned with priceless art, and the air is thick with secrets—old rivalries, hidden romances, and long-buried scandals.\n\nBut as the evening unfolds, tragedy strikes: the host is found dead, and the guests quickly realize that no one can be trusted. Each of you has a role to play in uncovering the truth, but beware—everyone has something to hide, and the walls of Blackwood Manor have ears.",
  participants: [
    {
      user: {
        uuid: "guest-uuid-2",
        name: "Bob Guest",
        avatarUrl: "https://randomuser.me/api/portraits/men/2.jpg",
      },
      character: {
        uuid: "char-uuid-2",
        name: "Eleanor Blackwood",
        role: "Hostess",
        shopDescription:
          "The enigmatic lady of the manor, Eleanor's calm hides a lifetime of secrets and intrigue.",
        avatarUrl: "https://randomuser.me/api/portraits/women/4.jpg",
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
        name: "Reginald Mustard",
        role: "Retired Colonel",
        shopDescription:
          "A retired military man, Reginald's stern exterior and cryptic stories make him respected and feared.",
        avatarUrl: "https://randomuser.me/api/portraits/men/5.jpg",
      },
    },
    {
      user: undefined,
      character: {
        uuid: "char-uuid-1",
        name: "Arthur Doyle",
        role: "Detective",
        shopDescription:
          "A sharp detective, Arthur is known for solving the most perplexing cases and uncovering the manor's secrets.",
        avatarUrl: "https://randomuser.me/api/portraits/men/6.jpg",
      },
    },
    {
      user: undefined,
      character: {
        uuid: "char-uuid-4",
        name: "Vivian Green",
        role: "Journalist",
        shopDescription:
          "A sharp-witted journalist, Vivian is always on the hunt for a scoop and has a knack for uncovering secrets others wish to keep hidden.",
        avatarUrl: "https://randomuser.me/api/portraits/women/7.jpg",
      },
    },
  ],
  assignments: [
    {
      characterId: "char-uuid-1",
      userId: undefined,
      inviteCode: "INVITE-DOYLE-2025",
    },
    {
      characterId: "char-uuid-2",
      userId: "guest-uuid-2",
      inviteCode: undefined,
    },
    {
      characterId: "char-uuid-3",
      userId: "guest-uuid-3",
      inviteCode: undefined,
    },
    {
      characterId: "char-uuid-4",
      userId: undefined,
      inviteCode: "INVITE-GREEN-2025",
    },
  ],
  allPrivateInfo: [
    {
      characterId: "char-uuid-1",
      privateDescription:
        "You are Arthur Doyle, a renowned detective invited to Blackwood Manor under the guise of a social visit.\n\nSecretly, you were contacted by an anonymous source who hinted at a crime about to take place. Your motivation is to solve the murder and restore your reputation after a recent case went unsolved.\n\nUnknown to the others, you once had a secret affair with Lady Eleanor Blackwood, and you must keep this hidden to avoid scandal and suspicion.\n\nYour keen observation skills and logical mind are your greatest assets, but trust no one—any guest could be the killer, and your past connection to the host could make you a target.",
    },
    {
      characterId: "char-uuid-2",
      privateDescription:
        "You are Lady Eleanor Blackwood, the enigmatic host of the evening. You have invited these guests to your ancestral home to resolve old debts and perhaps settle a few scores. Your motivation is to protect your family's reputation and keep your own secrets buried.\n\nYou are aware that Arthur Doyle, your former lover, is among the guests, and you must ensure your past affair remains hidden. You also suspect that someone among the guests is blackmailing you.\n\nMaintain your composure and use your influence to steer suspicion away from yourself.",
    },
    {
      characterId: "char-uuid-3",
      privateDescription:
        "You are Reginald Mustard, a retired military man with a stern exterior. You were invited to the manor because of your long-standing friendship with Lady Blackwood, but you have your own reasons for attending.\n\nYour motivation is to recover a valuable family heirloom you believe is hidden somewhere in the manor. You must keep your financial troubles secret from the other guests.\n\nUse your discipline and experience to observe the other guests and find the right moment to search for the heirloom.",
    },
    {
      characterId: "char-uuid-4",
      privateDescription:
        "You are Vivian Green, an ambitious journalist who has been invited to Blackwood Manor under the pretense of covering a society event.\n\nYour true motivation is to investigate rumors of corruption and scandal within the Blackwood family. You must tread carefully, as exposing the wrong secret could put you in danger.\n\nUse your charm and investigative skills to gather information, but beware—someone may be watching your every move.",
    },
  ],
  yourPrivateInfo: {
    characterId: "char-uuid-1",
    privateDescription:
      "You are Arthur Doyle, a renowned detective invited to Blackwood Manor under the guise of a social visit.\n\nSecretly, you were contacted by an anonymous source who hinted at a crime about to take place. Your motivation is to solve the murder and restore your reputation after a recent case went unsolved.\n\nUnknown to the others, you once had a secret affair with Lady Eleanor Blackwood, and you must keep this hidden to avoid scandal and suspicion.\n\nYour keen observation skills and logical mind are your greatest assets, but trust no one—any guest could be the killer, and your past connection to the host could make you a target.",
  },
};
