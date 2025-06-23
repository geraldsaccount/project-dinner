import GridLayout from "@/components/layout/grid-layout";
import PageHeader from "@/components/shared/page-header";
import { Button } from "@/components/ui/button";
import { useAuthenticatedApi } from "@/hooks";
import { DinnerSummaryDto } from "@/types";
import { useEffect } from "react";
import { Link } from "react-router-dom";
import DinnerSummaryCard from "./components/summary-card";
import DinnerSummaryCardSkeleton from "./components/summary-card-skeleton";

const DinnersPage = () => {
  const {
    callApi: fetchEvents,
    data: summaries,
    loading,
    error,
  } = useAuthenticatedApi<DinnerSummaryDto[]>();

  useEffect(() => {
    fetchEvents("/api/dinners");
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
          <DinnerSummaryCardSkeleton />
          <DinnerSummaryCardSkeleton />
          <DinnerSummaryCardSkeleton />
        </GridLayout>
      );
    }

    if (error || summaries === null) {
      return <div className="text-destructive">Something went wrong.</div>;
    }

    if (summaries.length === 0) {
      return (
        <div className="flex flex-col gap-4">
          <p>You have not attended any dinners yet.</p>
          <Button>
            <Link to={"host"}>Host Dinner</Link>
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
          <Link to={"host"}>Host new dinner</Link>
        </Button>
        {summaries.map((e) => (
          <DinnerSummaryCard key={e.uuid} summary={e} />
        ))}
      </GridLayout>
    );
  };

  return (
    <div className="flex flex-col gap-2 items-baseline">
      <div className="w-full flex justify-between">
        <PageHeader title="My Dinners" />
        <Link to="/invite" className="self-end">
          <Button>Join</Button>
        </Link>
      </div>
      {buildSummaries()}
    </div>
  );
};

export default DinnersPage;
