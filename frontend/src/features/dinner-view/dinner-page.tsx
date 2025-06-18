import DinnerHeader from "./components/dinner-header";
import DinnerStory from "./components/dinner-story";
import DinnerCast from "./components/dinner-cast";
import DinnerSecret from "./components/dinner-secret";
import { useParams } from "react-router-dom";
import { useAuthenticatedApi } from "@/hooks";
import { GuestDinnerViewDto, HostDinnerViewDto } from "@/types";
import { useEffect } from "react";
import LoadingHeader from "@/components/shared/loading-header";
import ErrorPage from "@/pages/error-page";
import HostCastChoice from "./components/host-cast-choice";

const DinnerPage = () => {
  const { dinnerId } = useParams();
  const {
    data: dinner,
    loading,
    error,
    callApi: fetchDinner,
  } = useAuthenticatedApi<HostDinnerViewDto | GuestDinnerViewDto>();
  useEffect(() => {
    if (dinnerId) {
      fetchDinner(`/api/dinners/${dinnerId}`);
    }
    // eslint-disable-next-line
  }, [dinnerId]);
  const isHostDinnerView = (
    dinner: HostDinnerViewDto | GuestDinnerViewDto
  ): dinner is HostDinnerViewDto => {
    return "assignments" in dinner;
  };

  if (loading) {
    return <LoadingHeader title="Loading Dinner..." />;
  }

  if (error || !dinnerId || !dinner) {
    return (
      <ErrorPage message="Could not load dinner informations."></ErrorPage>
    );
  }

  return (
    <div className="flex flex-col gap-8">
      <DinnerHeader dinner={dinner} />
      <DinnerStory dinner={dinner} />
      {isHostDinnerView(dinner) && !dinner.yourPrivateInfo ? (
        <HostCastChoice dinner={dinner} />
      ) : (
        <DinnerCast dinner={dinner} />
      )}
      {(!isHostDinnerView(dinner) || dinner.yourPrivateInfo) && (
        <DinnerSecret
          character={
            dinner.participants.find(
              (p) => p.character.uuid === dinner.yourPrivateInfo.characterId
            )!.character
          }
          secret={dinner.yourPrivateInfo}
        />
      )}
    </div>
  );
};

export default DinnerPage;
