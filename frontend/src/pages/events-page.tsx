import GridLayout from "@/components/layout/grid-layout";
import { Button } from "@/components/ui/button";
import type { SessionSummary } from "@/features/event-gallery/summary-card";
import SessionSummaryCard from "@/features/event-gallery/summary-card";
import SessionSummaryCardSkeleton from "@/features/event-gallery/summary-card-skeleton";
import { useAuthenticatedApi } from "@/hooks";
import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

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

  const navigate = useNavigate();

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
          <Button onClick={() => navigate("new")}>Host event</Button>
        </div>
      );
    }

    console.log(summaries);
    return (
      <GridLayout
        className="pt-4 w-full"
        gridCols={{ base: 1, sm: 2, md: 2, xl: 3 }}
      >
        <Button
          className="h-auto text-xl font-bold align-top"
          onClick={() => navigate("new")}
        >
          Host new event
        </Button>
        {summaries.map((e) => (
          <SessionSummaryCard key={e.sessionId} summary={e} />
        ))}
      </GridLayout>
    );
  };

  return (
    <div className="flex flex-col gap-2 items-baseline">
      <h2 className="text-3xl font-extrabold">My Events</h2>
      {buildSummaries()}
    </div>
  );
};

export default EventsPage;
