// import { Mystery } from "@/types/creation";

// const test: Mystery = {
//   story: {
//     id: "mystery-story-1",
//     title: "The Case of the Missing Sapphire",
//     shopDescription:
//       "A rare sapphire has vanished from the grand estate. Unravel the secrets and expose the thief in this thrilling mystery.",
//     bannerImage: null,
//     rules:
//       "Paragraph 1 for rules.\n\nParagraph 2 for rules.\n\nParagraph 3 for rules.",
//     setting:
//       "Paragraph 1 for setting.\n\nParagraph 2 for setting.\n\nParagraph 3 for setting.",
//     briefing:
//       "Paragraph 1 for briefing.\n\nParagraph 2 for briefing.\n\nParagraph 3 for briefing.",
//   },
//   characters: [
//     {
//       id: "char-1",
//       name: "Lady Eleanor",
//       role: "The Matriarch",
//       age: 68,
//       isPrimary: true,
//       gender: "Female",
//       shopDescription:
//         "Lady Eleanor, the formidable head of the family, holds many secrets within her elegant facade.",
//       privateDescription:
//         "Paragraph 1 for Lady Eleanor's private description.\n\nParagraph 2 for Lady Eleanor's private description.\n\nParagraph 3 for Lady Eleanor's private description.",
//       avatarImage: null,
//       relationships: {
//         "char-2": "Daughter",
//         "char-3": "Nephew",
//         "char-4": "Butler",
//       },
//       stageInfo: [
//         {
//           stageId: "stage-1",
//           order: 1,
//           objectivePrompt: "Investigate the drawing-room for clues.",
//           events: [
//             {
//               id: "char1-event1-stage1",
//               order: 1,
//               time: "7:00 PM",
//               title: "Initial Observation",
//               description:
//                 "Lady Eleanor notices the empty pedestal where the sapphire once rested.",
//             },
//             {
//               id: "char1-event2-stage1",
//               order: 2,
//               time: "7:30 PM",
//               title: "Questioning Staff",
//               description:
//                 "Lady Eleanor discreetly questions the household staff about any suspicious activity.",
//             },
//             {
//               id: "char1-event3-stage1",
//               order: 3,
//               time: "8:00 PM",
//               title: "Discovery of a Note",
//               description:
//                 "Lady Eleanor finds a cryptic note tucked beneath a cushion.",
//             },
//           ],
//         },
//         {
//           stageId: "stage-2",
//           order: 2,
//           objectivePrompt: "Follow up on the cryptic note.",
//           events: [
//             {
//               id: "char1-event1-stage2",
//               order: 1,
//               time: "9:00 PM",
//               title: "Deciphering the Clue",
//               description:
//                 "Lady Eleanor tries to decipher the meaning of the cryptic note.",
//             },
//             {
//               id: "char1-event2-stage2",
//               order: 2,
//               time: "9:30 PM",
//               title: "Confrontation",
//               description:
//                 "Lady Eleanor confronts one of the guests about a suspicious comment they made.",
//             },
//             {
//               id: "char1-event3-stage2",
//               order: 3,
//               time: "10:00 PM",
//               title: "Hidden Compartment",
//               description:
//                 "Lady Eleanor discovers a hidden compartment in the study.",
//             },
//           ],
//         },
//         {
//           stageId: "stage-3",
//           order: 3,
//           objectivePrompt: "Piece together the final clues.",
//           events: [
//             {
//               id: "char1-event1-stage3",
//               order: 1,
//               time: "11:00 PM",
//               title: "Revealing the Truth",
//               description:
//                 "Lady Eleanor reveals a crucial piece of information that ties everything together.",
//             },
//             {
//               id: "char1-event2-stage3",
//               order: 2,
//               time: "11:30 PM",
//               title: "Accusation",
//               description:
//                 "Lady Eleanor makes a direct accusation based on her findings.",
//             },
//             {
//               id: "char1-event3-stage3",
//               order: 3,
//               time: "12:00 AM",
//               title: "Resolution",
//               description:
//                 "Lady Eleanor participates in the final reveal of the culprit.",
//             },
//           ],
//         },
//       ],
//     },
//     {
//       id: "char-2",
//       name: "Mr. Charles",
//       role: "The Eccentric Collector",
//       age: 45,
//       isPrimary: true,
//       gender: "Male",
//       shopDescription:
//         "Mr. Charles, a renowned art collector, has a peculiar fascination with rare artifacts.",
//       privateDescription:
//         "Paragraph 1 for Mr. Charles' private description.\n\nParagraph 2 for Mr. Charles' private description.\n\nParagraph 3 for Mr. Charles' private description.",
//       avatarImage: null,
//       relationships: {
//         "char-1": "Acquaintance",
//         "char-3": "Rival Collector",
//       },
//       stageInfo: [
//         {
//           stageId: "stage-1",
//           order: 1,
//           objectivePrompt: "Observe other guests and their reactions.",
//           events: [
//             {
//               id: "char2-event1-stage1",
//               order: 1,
//               time: "7:00 PM",
//               title: "Initial Shock",
//               description:
//                 "Mr. Charles expresses his shock at the news of the missing sapphire.",
//             },
//             {
//               id: "char2-event2-stage1",
//               order: 2,
//               time: "7:45 PM",
//               title: "Suspicious Glance",
//               description:
//                 "Mr. Charles notices a suspicious glance between two other guests.",
//             },
//             {
//               id: "char2-event3-stage1",
//               order: 3,
//               time: "8:15 PM",
//               title: "Overheard Conversation",
//               description:
//                 "Mr. Charles overhears a hushed conversation about a hidden passage.",
//             },
//           ],
//         },
//         {
//           stageId: "stage-2",
//           order: 2,
//           objectivePrompt: "Investigate the hidden passage.",
//           events: [
//             {
//               id: "char2-event1-stage2",
//               order: 1,
//               time: "9:15 PM",
//               title: "Searching for the Passage",
//               description:
//                 "Mr. Charles actively searches for the rumored hidden passage.",
//             },
//             {
//               id: "char2-event2-stage2",
//               order: 2,
//               time: "9:45 PM",
//               title: "Finding a Clue",
//               description:
//                 "Mr. Charles finds a small, unusual trinket near the fireplace.",
//             },
//             {
//               id: "char2-event3-stage2",
//               order: 3,
//               time: "10:15 PM",
//               title: "Misleading Information",
//               description:
//                 "Mr. Charles is given misleading information by another character.",
//             },
//           ],
//         },
//         {
//           stageId: "stage-3",
//           order: 3,
//           objectivePrompt: "Analyze the trinket and confront suspect.",
//           events: [
//             {
//               id: "char2-event1-stage3",
//               order: 1,
//               time: "11:15 PM",
//               title: "Trinket's Origin",
//               description:
//                 "Mr. Charles determines the origin of the unusual trinket.",
//             },
//             {
//               id: "char2-event2-stage3",
//               order: 2,
//               time: "11:45 PM",
//               title: "Confrontation with the Thief",
//               description:
//                 "Mr. Charles confronts a character he suspects of the theft.",
//             },
//             {
//               id: "char2-event3-stage3",
//               order: 3,
//               time: "12:15 AM",
//               title: "Unmasking the Culprit",
//               description:
//                 "Mr. Charles contributes to the unmasking of the true culprit.",
//             },
//           ],
//         },
//       ],
//     },
//     {
//       id: "char-3",
//       name: "Miss Isabella",
//       role: "The Aspiring Author",
//       age: 28,
//       isPrimary: true,
//       gender: "Female",
//       shopDescription:
//         "Miss Isabella, a curious and observant writer, is always looking for inspiration for her next novel.",
//       privateDescription:
//         "Paragraph 1 for Miss Isabella's private description.\n\nParagraph 2 for Miss Isabella's private description.\n\nParagraph 3 for Miss Isabella's private description.",
//       avatarImage: null,
//       relationships: {
//         "char-1": "Niece",
//         "char-2": "Fellow Guest",
//       },
//       stageInfo: [
//         {
//           stageId: "stage-1",
//           order: 1,
//           objectivePrompt: "Interview guests about their alibis.",
//           events: [
//             {
//               id: "char3-event1-stage1",
//               order: 1,
//               time: "7:15 PM",
//               title: "Initial Questions",
//               description:
//                 "Miss Isabella begins to informally interview guests about their whereabouts.",
//             },
//             {
//               id: "char3-event2-stage1",
//               order: 2,
//               time: "7:50 PM",
//               title: "Suspicious Alibi",
//               description:
//                 "Miss Isabella finds a inconsistency in one guest's alibi.",
//             },
//             {
//               id: "char3-event3-stage1",
//               order: 3,
//               time: "8:20 PM",
//               title: "Hidden Document",
//               description:
//                 "Miss Isabella finds a discarded document that seems out of place.",
//             },
//           ],
//         },
//         {
//           stageId: "stage-2",
//           order: 2,
//           objectivePrompt: "Investigate the suspicious alibi and document.",
//           events: [
//             {
//               id: "char3-event1-stage2",
//               order: 1,
//               time: "9:20 PM",
//               title: "Cross-Referencing Information",
//               description:
//                 "Miss Isabella cross-references the document with conversations she's had.",
//             },
//             {
//               id: "char3-event2-stage2",
//               order: 2,
//               time: "9:50 PM",
//               title: "Witness Testimony",
//               description:
//                 "Miss Isabella seeks out a minor witness who saw something unusual.",
//             },
//             {
//               id: "char3-event3-stage2",
//               order: 3,
//               time: "10:20 PM",
//               title: "Tracing the Document",
//               description:
//                 "Miss Isabella attempts to trace the origin of the hidden document.",
//             },
//           ],
//         },
//         {
//           stageId: "stage-3",
//           order: 3,
//           objectivePrompt: "Expose the truth with your findings.",
//           events: [
//             {
//               id: "char3-event1-stage3",
//               order: 1,
//               time: "11:20 PM",
//               title: "Presenting the Evidence",
//               description:
//                 "Miss Isabella presents her compiled evidence to the group.",
//             },
//             {
//               id: "char3-event2-stage3",
//               order: 2,
//               time: "11:50 PM",
//               title: "Interrogating the Suspect",
//               description:
//                 "Miss Isabella directly interrogates the character with the suspicious alibi.",
//             },
//             {
//               id: "char3-event3-stage3",
//               order: 3,
//               time: "12:20 AM",
//               title: "Final Deduction",
//               description:
//                 "Miss Isabella makes a crucial final deduction that aids the investigation.",
//             },
//           ],
//         },
//       ],
//     },
//     {
//       id: "char-4",
//       name: "Mr. Peterson",
//       role: "The Reserved Butler",
//       age: 55,
//       isPrimary: false,
//       gender: "Male",
//       shopDescription:
//         "Mr. Peterson, the long-serving butler, knows the estate and its inhabitants better than anyone.",
//       privateDescription:
//         "Paragraph 1 for Mr. Peterson's private description.\n\nParagraph 2 for Mr. Peterson's private description.\n\nParagraph 3 for Mr. Peterson's private description.",
//       avatarImage: null,
//       relationships: {
//         "char-1": "Employer",
//         "char-2": "Guest",
//         "char-3": "Guest",
//       },
//       stageInfo: [
//         {
//           stageId: "stage-1",
//           order: 1,
//           objectivePrompt: "Assist guests and observe their behavior.",
//           events: [
//             {
//               id: "char4-event1-stage1",
//               order: 1,
//               time: "7:00 PM",
//               title: "Serving Drinks",
//               description:
//                 "Mr. Peterson meticulously serves drinks, observing each guest's demeanor.",
//             },
//             {
//               id: "char4-event2-stage1",
//               order: 2,
//               time: "7:40 PM",
//               title: "Noticing an Anomaly",
//               description:
//                 "Mr. Peterson notices a small, out-of-place item near the sapphire's display.",
//             },
//             {
//               id: "char4-event3-stage1",
//               order: 3,
//               time: "8:10 PM",
//               title: "Cleaning Up",
//               description:
//                 "Mr. Peterson tidies up the room, finding a faint scuff mark on the floor.",
//             },
//           ],
//         },
//         {
//           stageId: "stage-2",
//           order: 2,
//           objectivePrompt: "Provide discreet assistance to investigators.",
//           events: [
//             {
//               id: "char4-event1-stage2",
//               order: 1,
//               time: "9:10 PM",
//               title: "Offering Information",
//               description:
//                 "Mr. Peterson subtly offers a piece of information he observed earlier.",
//             },
//             {
//               id: "char4-event2-stage2",
//               order: 2,
//               time: "9:40 PM",
//               title: "Recalling a Detail",
//               description:
//                 "Mr. Peterson recalls a specific detail about a guest's movements.",
//             },
//             {
//               id: "char4-event3-stage2",
//               order: 3,
//               time: "10:10 PM",
//               title: "Locating a Tool",
//               description:
//                 "Mr. Peterson knows where a specific tool, potentially used in the crime, is kept.",
//             },
//           ],
//         },
//         {
//           stageId: "stage-3",
//           order: 3,
//           objectivePrompt: "Share your intimate knowledge of the estate.",
//           events: [
//             {
//               id: "char4-event1-stage3",
//               order: 1,
//               time: "11:10 PM",
//               title: "Revealing a Secret Passage",
//               description:
//                 "Mr. Peterson reveals the existence of a rarely used secret passage.",
//             },
//             {
//               id: "char4-event2-stage3",
//               order: 2,
//               time: "11:40 PM",
//               title: "Confirming Alibis",
//               description:
//                 "Mr. Peterson can confirm or refute certain alibis based on his knowledge of the house.",
//             },
//             {
//               id: "char4-event3-stage3",
//               order: 3,
//               time: "12:10 AM",
//               title: "Final Observation",
//               description:
//                 "Mr. Peterson makes a final, subtle observation that helps confirm the culprit's identity.",
//             },
//           ],
//         },
//       ],
//     },
//   ],
//   stages: [
//     {
//       id: "stage-1",
//       order: 1,
//       title: "The Discovery",
//       hostPrompt:
//         "Paragraph 1 for host prompt stage 1.\n\nParagraph 2 for host prompt stage 1.",
//     },
//     {
//       id: "stage-2",
//       order: 2,
//       title: "The Investigation",
//       hostPrompt:
//         "Paragraph 1 for host prompt stage 2.\n\nParagraph 2 for host prompt stage 2.",
//     },
//     {
//       id: "stage-3",
//       order: 3,
//       title: "The Revelation",
//       hostPrompt:
//         "Paragraph 1 for host prompt stage 3.\n\nParagraph 2 for host prompt stage 3.",
//     },
//   ],
//   setups: [
//     {
//       id: "setup-3-players",
//       playerCount: 3,
//       characterIds: ["char-1", "char-2", "char-3"],
//     },
//     {
//       id: "setup-4-players",
//       playerCount: 4,
//       characterIds: ["char-1", "char-2", "char-3", "char-4"],
//     },
//   ],
//   crime: {
//     criminalIds: ["char-3"],
//     description:
//       "Paragraph 1 for crime description.\n\nParagraph 2 for crime description.\n\nParagraph 3 for crime description.",
//   },
// };
