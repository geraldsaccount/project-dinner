import GridLayout from "@/components/layout/grid-layout";
import PageHeader from "@/components/shared/page-header";
import { Button } from "@/components/ui/button";
import type { SessionSummary } from "@/features/event-gallery/components/summary-card";
import SessionSummaryCard from "@/features/event-gallery/components/summary-card";
import SessionSummaryCardSkeleton from "@/features/event-gallery/components/summary-card-skeleton";
import { useAuthenticatedApi } from "@/hooks";
import { useEffect } from "react";
import { Link } from "react-router-dom";

const EventsPage = () => {
  const {
    callApi: fetchEvents,
    data: summaries,
    loading,
    error,
  } = useAuthenticatedApi<SessionSummary[]>();

  useEffect(() => {
    fetchEvents("/api/sessions");
  }, [fetchEvents]);

  // const navigate = useNavigate();
  // const navigateToCreationPage = () => navigate("create");

  const buildSummaries = () => {
    if (loading) {
      return (
        <GridLayout
          className="pt-4 w-full"
          gridCols={{ base: 1, sm: 2, md: 2, xl: 3 }}
        >
          <SessionSummaryCardSkeleton />
          <SessionSummaryCardSkeleton />
          <SessionSummaryCardSkeleton />
        </GridLayout>
      );
    }

    if (error || summaries === null) {
      return <div className="text-destructive">Something went wrong.</div>;
    }

    if (summaries.length === 0) {
      return (
        <div className="flex flex-col gap-4">
          <p>You have not attended any events yet.</p>
          <Button>
            <Link to={"create"}>Host event</Link>
          </Button>
        </div>
      );
    }

    return (
      <GridLayout
        className="pt-4 w-full"
        gridCols={{ base: 1, sm: 2, md: 2, xl: 3 }}
      >
        <Button className="h-auto text-xl font-bold align-top">
          <Link to={"create"}>Host new event</Link>
        </Button>
        {summaries.map((e) => (
          <SessionSummaryCard key={e.sessionId} summary={e} />
        ))}
      </GridLayout>
    );
  };

  return (
    <div className="flex flex-col gap-2 items-baseline">
      <PageHeader title="My Events" />
      {buildSummaries()}
    </div>
  );
};

export default EventsPage;
