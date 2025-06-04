import { Card, CardContent, CardHeader } from "@/components/ui/card";
import { Skeleton } from "@/components/ui/skeleton";

const StoryCardSkeleton = () => (
  <Card className="py-0 overflow-clip h-full">
    <div className="flex h-full">
      <div className="w-1/3 flex-shrink-0 flex items-center justify-center">
        <Skeleton className="rounded-none rounded-l-md w-full h-full bg-muted-foreground" />
      </div>
      <div className="w-2/3 p-4">
        <CardHeader className="p-0 pb-2">
          <Skeleton className="h-6 w-2/3 mb-2 bg-muted-foreground" />
          <Skeleton className="h-4 w-1/3 bg-muted-foreground" />
        </CardHeader>
        <CardContent className="p-0">
          <Skeleton className="h-4 w-3/4 bg-muted-foreground mb-1" />
          <Skeleton className="h-4 w-3/4 bg-muted-foreground mb-1" />
          <Skeleton className="h-4 w-1/2 bg-muted-foreground" />
        </CardContent>
      </div>
    </div>
  </Card>
);

export default StoryCardSkeleton;
