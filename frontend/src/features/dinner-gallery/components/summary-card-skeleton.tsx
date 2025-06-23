import { Card, CardContent, CardHeader } from "@/components/ui/card";
import { Skeleton } from "@/components/ui/skeleton";

const DinnerSummaryCardSkeleton = () => (
  <Card className="py-0 overflow-clip">
    <div className="flex">
      <div className="w-1/3 flex-shrink-0 flex items-center justify-center">
        <Skeleton className="rounded-none w-full h-full bg-muted-foreground" />
      </div>
      <div className="w-2/3 p-4">
        <CardHeader className="p-0 pb-2 bg-transparent border-none">
          <Skeleton className="h-6 w-2/3 mb-2 bg-muted-foreground" />
          <Skeleton className="h-4 w-1/3 bg-muted-foreground" />
        </CardHeader>
        <CardContent className="p-0 space-y-2">
          <Skeleton className="h-4 w-1/2 bg-muted-foreground" />
          <Skeleton className="h-4 w-2/3 bg-muted-foreground" />
        </CardContent>
      </div>
    </div>
  </Card>
);

export default DinnerSummaryCardSkeleton;
