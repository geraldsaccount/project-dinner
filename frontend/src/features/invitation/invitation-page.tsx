import PageHeader from "@/components/shared/page-header";
import { sampleInvitation } from "@/data/sample-invitation";
import { Button } from "@/components/ui/button";
import { SignedIn, SignedOut, SignInButton } from "@clerk/clerk-react";
import HostCallout from "./components/host-callout";
import CastGrid from "./components/cast-grid";
import YourCharacter from "./components/your-character";
import AlreadyPlayedMessage from "./components/already-played-message";

const InvitationPage = () => {
  const invitation = sampleInvitation;
  const sortedParticipants = [
    ...invitation.otherParticipants.filter(
      (p) => p.character.uuid !== invitation.yourAssignedCharacter.uuid
    ),
    ...invitation.otherParticipants.filter(
      (p) => p.character.uuid === invitation.yourAssignedCharacter.uuid
    ),
  ];
  return (
    <div className="flex flex-col gap-4">
      <PageHeader title="You're invited to dinner!" />
      <HostCallout host={invitation.host} dateTime={invitation.dateTime} />
      <div className="relative w-full h-40 sm:h-60 md:h-80 lg:h-96 mb-4 rounded-xl shadow overflow-hidden">
        <img
          src={invitation.storyBannerUrl}
          alt={invitation.storyTitle}
          className="w-full h-full object-cover absolute inset-0"
        />
        <div className="absolute inset-0 bg-gradient-to-t from-primary/70 via-primary/30 to-transparent" />
        <div className="absolute bottom-0 left-0 w-full p-4 text-primary-foreground">
          <h3 className="text-2xl font-extrabold tracking-tight drop-shadow-lg">
            {invitation.storyTitle}
          </h3>
          <div className="text-base font-medium drop-shadow-lg">
            {invitation.dinnerStoryBrief}
          </div>
        </div>
      </div>
      <div className="flex flex-col">
        <h3 className="text-2xl font-extrabold">Meet the Cast</h3>
        <CastGrid
          participants={sortedParticipants}
          yourCharacterUuid={invitation.yourAssignedCharacter.uuid}
        />
        <YourCharacter character={invitation.yourAssignedCharacter} />
        {invitation.canAccept ? (
          <>
            <SignedIn>
              <Button className="w-full sm:w-auto self-start mt-2">
                Accept Invitation
              </Button>
            </SignedIn>
            <SignedOut>
              <div className="flex mt-2 gap-2">
                <Button className="w-full sm:w-auto" disabled>
                  Accept Invitation
                </Button>
                <SignInButton>
                  <Button variant="outline">Log In to Accept Invitation</Button>
                </SignInButton>
              </div>
            </SignedOut>
          </>
        ) : (
          <div className="flex mt-2 gap-2">
            <AlreadyPlayedMessage />
          </div>
        )}
      </div>
    </div>
  );
};

export default InvitationPage;
