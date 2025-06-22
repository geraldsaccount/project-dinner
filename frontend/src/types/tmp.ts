export interface GuestDinnerViewDto {
  preDinnerInfo: PreDinnerInfoDto; // each user will get the same predinner information
  privateInfo: PrivateInfoDto; // information specific to the current user
  conclusion: ConclusionDto; // is null when voting stage not reached yet
}

export interface HostDinnerViewDto {
  preDinnerInfo: PreDinnerInfoDto;
  privateInfo: PrivateInfoDto;
  conclusion: ConclusionDto;
  hostInfo: HostInfoDto;
}

export interface PreDinnerInfoDto {
  uuid: string;
  dateTime: string; // date and time of the dinner
  host: UserDto; // the host
  storyTitle: string; // title of the mystery that is played
  storyBannerData: string; // image data for the banner
  setting: string; // backgorund description of the story, how the characters got there etc (will contain multiple paragraphs)
  rules: string; // tips on how to prepare for the evening and rules on how the dinner will unfold (will contain multiple paragraphs)
  participants: DinnerParticipantDto[];
}

export interface UserDto {
  uuid: string;
  name: string; // will be either a username or Firstname initial lastname
  avatarUrl: string; // url to the oauth avatar
}

export interface DinnerParticipantDto {
  user: UserDto | undefined;
  character: CharacterDetailDto;
}

export interface CharacterDetailDto {
  uuid: string;
  name: string;
  age: number;
  shopDescription: string; // can be ignored for now
  avatarData: string; // image data for the avatar
  role: string; // archetype "e.g. disgruntled buttler"
}

export interface PrivateInfoDto {
  characterId: string; // id of the character the user is playing to reference participants.character.uuid
  characterDescription: string; // private description of the character (will contain multiple paragraphs)
  relationships: Record<string, string>; // record for relationships to the other characters key: character id value: text on relationship (will contain max 2 paragraphs)
  stages: CharacterStageDto[]; // the dinners have multiple rounds. the host will initiate each round, but we dont care about this right now. before the host starts the dinner this will be null. with progressing dinner this list will increase with each round
}

export interface CharacterStageDto {
  stageTitle: string; // the global title of the stage. just some general title
  objectiveDesc: string; // short description of an objective the user can follow during the stage. should be shown after the events
  events: StageEventDto[]; // events the users character has had during the timespan of the round/ could be shown like a timeline from top to bottom
}

export interface StageEventDto {
  time: string; // time of the event
  title: string; // snappy title for the event
  description: string; // description of what happened / what the character has seen (will contain max 1 paragraph)
}

export interface ConclusionDto {
  voteOpen: boolean; // is true when in voting phase
  criminalIds: string[]; // is null when in voting phase
  motive: string; // is null when in voting phase
  votes: FinalVoteDto[]; // contains the votes casted
}

export interface FinalVoteDto {
  guestId: string; // user
  suspectId: string; // character
  motive: string;
}

export interface HostInfoDto {
  briefing: string;
  assignments: CharacterAssignmentDto[];
  missingPrivateInfo: PrivateInfoDto[];
  stagePrompts: string[];
  allHaveVoted: boolean;
}

export interface CharacterAssignmentDto {
  characterId: string;
  userId?: string; // Optional because a character might not be assigned yet
  inviteCode?: string; // Optional because empty if user has accepted invititation
}
