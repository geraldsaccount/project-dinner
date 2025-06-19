import { Card, CardContent, CardHeader } from "@/components/ui/card";
import { cn } from "@/lib/utils";
import type { StorySummaryDto } from "@/types";

type Props = {
  summary: StorySummaryDto;
  minPlayerCount: number;
  maxPlayerCount: number;
  className?: string;
};

const StoryCard = ({ summary, minPlayerCount, maxPlayerCount, className }: Props) => {
  const { title, thumbnailDescription } = summary;
  const description = thumbnailDescription;
  const fixedPlayerCount = minPlayerCount === maxPlayerCount;

  return (
    <Card className={cn("py-0 overflow-clip h-full", className)}>
      <div className="flex h-full">
        <div className="w-1/3 flex-shrink-0 flex items-center justify-center bg-gray-100">
          <img
            src={`data:image/jpeg;base64,${summary.bannerData}`}
            alt={title}
            className="object-cover w-full h-full"
          />
        </div>
        <div className="w-2/3 p-4">
          <CardHeader className="p-0 pb-2">
            <div className="font-bold text-lg">{title}</div>
            <div className="text-sm text-muted-foreground">
              {fixedPlayerCount ? minPlayerCount : `${minPlayerCount} - ${maxPlayerCount}`}{" "}
              Players
            </div>
          </CardHeader>
          <CardContent className="p-0">
            <p className="font-medium">{description}</p>
          </CardContent>
        </div>
      </div>
    </Card>
  );
};

export default StoryCard;
