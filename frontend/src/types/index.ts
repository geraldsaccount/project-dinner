export interface NavItem {
  title: string;
  to: string;
  disabled?: boolean;
}

export interface MobileNavProps {
  items: NavItem[];
  activeItem: string;
  setActiveItem: (to: string) => void;
}
/**
 * The response given when a dinner was successfully created.
 */
export interface NewDinnerDTO {
  dinnerId: string;
}

/**
 * Represents basic, publicly available information about a user.
 */
export interface UserDto {
  uuid: string;
  name: string;
  avatarUrl: string;
}

/**
 * A minimal representation of a character, typically for lists or summaries.
 */
export interface CharacterSummaryDto {
  uuid: string;
  name: string;
  role: string;
}

/**
 * A minimal representation of a story, used for shop thumbnails or lists.
 */
export interface StorySummaryDto {
  //   uuid: string;
  title: string;
  bannerData: string;
  thumbnailDescription: string;
}

/**
 * A more detailed character representation, including its public description.
 * Used in the story detail page and the new invitation view.
 */
export interface CharacterDetailDto {
  uuid: string;
  name: string;
  age: number;
  shopDescription: string;
  avatarData: string;
  role: string;
}

/**
 * The full, public-facing details of a story for the shop page.
 */
export interface StoryDetailDto {
  uuid: string;
  title: string;
  shopDescription: string;
  bannerData: string;
  minPlayerCount: number;
  maxPlayerCount: number;
  characters: CharacterDetailDto[];
}

/**
 * Represents the private, secret information for a character in a dinner.
 * Should only be present for the logged-in user or the host.
 */
export interface PrivateCharacterInfo {
  characterId: string;
  privateDescription: string;
}

/**
 * A lightweight summary of a dinner for the "My Dinners" list.
 */
export interface DinnerSummaryDto {
  uuid: string;
  dateTime: string; // ISO 8601 string format
  host: UserDto;
  storyTitle: string;
  storyBannerData: string;
  yourAssignedCharacterName: string;
}

/**
 * Represents a participant, linking a user to their assigned character.
 * Used for the "starring" view in both the dinner and invitation pages.
 */
export interface DinnerParticipantDto {
  user: UserDto | undefined;
  character: CharacterDetailDto;
}

/**
 * The primary interface for a GUEST viewing the dinner page.
 */
export interface GuestDinnerViewDto {
  preDinnerInfo: PreDinnerInfoDto;
  privateInfo: PrivateInfoDto;
  conclusion: ConclusionDto;
}

/**
 * Represents a single character assignment, including the invite code.
 * This is intended for the HOST only.
 */
export interface CharacterAssignmentDto {
  characterId: string;
  userId?: string; // Optional because a character might not be assigned yet
  inviteCode?: string;
}

/**
 * The interface for a HOST viewing the dinner page.
 * Contains all guest info PLUS host-specific management data.
 */
export interface HostDinnerViewDto {
  preDinnerInfo: PreDinnerInfoDto;
  privateInfo: PrivateInfoDto | undefined;
  conclusion: ConclusionDto;
  hostInfo: HostInfoDto;
}

/**
 * For the invitation page, viewed by a user with an invite code for a specific character.
 * Provides context about the story, the character they will play,
 * who else is playing, and whether they are eligible to accept.
 */
export interface InvitationViewDto {
  inviteCode: string;
  dateTime: string;
  host: UserDto;
  storyTitle: string;
  storyBannerData: string;
  dinnerStoryBrief: string;
  yourAssignedCharacter: CharacterDetailDto;
  otherParticipants: DinnerParticipantDto[];
  canAccept: boolean;
}

export interface PreDinnerInfoDto {
  uuid: string;
  dateTime: string;
  host: UserDto;
  storyTitle: string;
  storyBannerData: string;
  setting: string;
  rules: string;
  participants: DinnerParticipantDto[];
}

export interface PrivateInfoDto {
  characterId: string;
  characterDescription: string;
  relationships: Record<string, string>;
  stages: CharacterStageDto[];
}

export interface ConclusionDto {
  voteOpen: boolean;
  criminalIds: string[];
  motive: string;
  votes: FinalVoteDto[];
}

export interface HostInfoDto {
  briefing: string;
  assignments: CharacterAssignmentDto[];
  missingPrivateInfo: PrivateInfoDto[];
  stagePrompts: string;
  allHaveVoted: boolean;
}

/**
 * Represents the different genders for character assignment configurations.
 */
export type Gender = "MALE" | "FEMALE" | "ANY";
export const Gender = {
  MALE: "MALE" as Gender,
  FEMALE: "FEMALE" as Gender,
  ANY: "ANY" as Gender,
};

/**
 * Represents a specific configuration for a story, e.g., a certain number of players.
 */
export interface ConfigDto {
  id: string;
  playerCount: number;
  genderCounts: Record<Gender, number>;
  characterIds: string[];
}

export interface StoryForCreationDto {
  uuid: string;
  story: StorySummaryDto;
  minPlayerCount: number;
  maxPlayerCount: number;
  characters: CharacterDetailDto[];
  configs: ConfigDto[];
}

export interface CharacterStageDto {
  stageTitle: string;
  objectiveDesc: string;
  events: StageEventDto[];
}

export interface StageEventDto {
  time: string;
  title: string;
  description: string;
}

export interface FinalVoteDto {
  guestId: string; // user
  suspectIds: string[]; // character
  motive: string;
}
