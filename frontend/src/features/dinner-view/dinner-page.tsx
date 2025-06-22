import DinnerHeader from "./components/dinner-header";
import { GuestDinnerViewDto, HostDinnerViewDto } from "@/types";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import PublicTab from "./tabs/public-tab";
import SecretTab from "./tabs/secret-tab";
import StagesTab from "./tabs/stages-tab";
import ConclusionTab from "./tabs/conclusion-tab";
import HostTab from "./tabs/host-tab";
import { sampleHostDinnerView } from "@/data/sample-dinner-views";
import VoteTab from "./tabs/vote-tab";
import { useParams } from "react-router-dom";
import { useAuthenticatedApi } from "@/hooks";
import { useEffect } from "react";
import LoadingHeader from "@/components/shared/loading-header";
import ErrorPage from "@/pages/error-page";

type TabId = "host" | "public" | "secret" | "stages" | "vote" | "conclusion";
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
//   const dinner = sampleHostDinnerView;
  const isHostDinnerView = (
    dinner: HostDinnerViewDto | GuestDinnerViewDto
  ): dinner is HostDinnerViewDto => {
    return "hostInfo" in dinner;
  };

  if (loading) {
    return <LoadingHeader title="Loading Dinner..." />;
  }

  if (error || !dinnerId || !dinner) {
    return (
      <ErrorPage message="Could not load dinner informations."></ErrorPage>
    );
  }
  const tabItems = () => {
    const items = [];
    if (isHostDinnerView(dinner)) {
      items.push({ id: "host", label: "Host" });
    }
    items.push({ id: "public", label: "Public Information" });

    if (dinner.privateInfo) {
      items.push({ id: "secret", label: "Secret" });
      if (dinner.privateInfo.stages && dinner.privateInfo.stages.length > 0) {
        items.push({ id: "stages", label: "Evening Information" });
      }
    }

    if (dinner.conclusion) {
      items.push(
        dinner.conclusion.voteOpen
          ? { id: "vote", label: "Vote" }
          : { id: "conclusion", label: "Conclusion" }
      );
    }

    return items;
  };

  return (
    <div className="flex flex-col gap-8 max-w-6xl ">
      <DinnerHeader preDinnerInfo={dinner.preDinnerInfo} />
      <Tabs
        defaultValue={isHostDinnerView(dinner) ? "host" : "public"}
        className="w-full space-y-4"
      >
        <TabsList className="w-full" style={{ justifyContent: "flex-start" }}>
          {tabItems().map((item) => (
            <TabsTrigger key={item.id} value={item.id as TabId}>
              {item.label}
            </TabsTrigger>
          ))}
        </TabsList>

{
    isHostDinnerView(dinner) && 
        <TabsContent value="host" className="border-primary">
          <HostTab dinner={dinner} />
        </TabsContent>
}
        <TabsContent value="public" className="border-primary">
          <PublicTab preDinnerInfo={dinner.preDinnerInfo} />
        </TabsContent>

        {dinner.privateInfo && (
          <TabsContent value="secret" className="border-primary">
            <SecretTab
              participants={dinner.preDinnerInfo.participants}
              privateInfo={dinner.privateInfo}
            />
          </TabsContent>
        )}
        {dinner.privateInfo?.stages && dinner.privateInfo.stages.length > 0 && (
          <TabsContent value="stages" className="border-primary">
            <StagesTab stages={dinner.privateInfo.stages} />
          </TabsContent>
        )}
        {dinner.conclusion && dinner.privateInfo && (
          <TabsContent value="vote" className="border-primary">
            <VoteTab
              dinnerId={dinner.preDinnerInfo.uuid}
              userId={
                dinner.preDinnerInfo.participants.find(
                  (p) => p.character.uuid === dinner.privateInfo?.characterId
                )?.user?.uuid
              }
              participants={dinner.preDinnerInfo.participants}
              conclusion={dinner.conclusion}
            />
          </TabsContent>
        )}
        {dinner.conclusion && (
          <TabsContent value="conclusion" className="border-primary">
            <ConclusionTab
              preDinnerInfo={dinner.preDinnerInfo}
              conclusion={dinner.conclusion}
            />
          </TabsContent>
        )}
      </Tabs>
    </div>
  );
};

export default DinnerPage;
