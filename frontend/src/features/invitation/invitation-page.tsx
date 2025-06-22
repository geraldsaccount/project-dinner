import PageHeader from "@/components/shared/page-header";
import { Button } from "@/components/ui/button";
import { SignedIn, SignedOut, SignInButton } from "@clerk/clerk-react";
import HostCallout from "./components/host-callout";
import CastGrid from "./components/cast-grid";
import YourCharacter from "./components/your-character";
import AlreadyPlayedMessage from "./components/already-played-message";
import StoryBanner from "./components/story-banner";
import useApi from "@/hooks/useApi";
import { InvitationViewDto, NewDinnerDTO } from "@/types";
import { useParams, useNavigate } from "react-router-dom";
import { useEffect } from "react";
import { InviteCodeForm } from "./invite-code-page";
import LoadingHeader from "@/components/shared/loading-header";
import ErrorPage from "@/pages/error-page";
import { useAuthenticatedApi } from "@/hooks";
import { toast } from "sonner";
import SectionHeader from "@/components/shared/section-header";

const InvitationPage = () => {
  const { inviteCode } = useParams();
  const navigate = useNavigate();
  const {
    data: invitation,
    loading,
    error,
    callApi: fetchApi,
  } = useApi<InvitationViewDto>();
  const {
    loading: accepting,
    error: acceptFail,
    callApi: putAccept
  } = useAuthenticatedApi<NewDinnerDTO>();

  useEffect(() => {
    if (inviteCode) {
      fetchApi(`/api/invite/${inviteCode}`);
    }
    // eslint-disable-next-line
  }, [inviteCode]);

  function handleRetry(code: string) {
    fetchApi(`/api/invite/${code}`);
  }

  if (loading) {
    return <LoadingHeader title="Loading invitation..." />;
  }

  if (error || !invitation) {
    return (
      <ErrorPage message="Could not load invitation.">
        <InviteCodeForm onSubmit={handleRetry} />
      </ErrorPage>
    );
  }

  const sortedParticipants = [
    ...invitation.otherParticipants.filter(
      (p) => p.character.uuid !== invitation.yourAssignedCharacter.uuid
    ),
    ...invitation.otherParticipants.filter(
      (p) => p.character.uuid === invitation.yourAssignedCharacter.uuid
    ),
  ];

  const acceptInvitation = async () => {
    console.log("accepting")
    const dinner = await putAccept(`/api/invite/${inviteCode}`, "PUT");
    if (acceptFail) {
      toast.error("Something went wrong.");
    } else if (dinner && dinner.dinnerId) {
      navigate(`/dinners/${dinner.dinnerId}`);
    }
  }

  return (
    <div className="space-y-4">
      <PageHeader title="You're invited to dinner!" />
      <HostCallout host={invitation.host} dateTime={invitation.dateTime} />
      <StoryBanner invitation={invitation} />
        <section>
            <SectionHeader title="Meet the Cast" />
            <CastGrid
            participants={sortedParticipants}
            yourCharacterUuid={invitation.yourAssignedCharacter.uuid}
            />
        </section>
        
        <YourCharacter character={invitation.yourAssignedCharacter} />
        {invitation.canAccept ? (
          <>
            <SignedIn>
              <Button className="w-full sm:w-auto self-start mt-2" disabled={accepting} onClick={acceptInvitation}>
                Accept Invitation
              </Button>
            </SignedIn>
            <SignedOut>
              <div className="flex mt-2 gap-2">
                <Button className="w-full sm:w-auto" disabled onClick={acceptInvitation}>
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
      <div>
      </div>
    </div>
  );
};

export default InvitationPage;
