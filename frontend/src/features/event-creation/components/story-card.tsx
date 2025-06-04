import { Card, CardContent, CardHeader } from "@/components/ui/card";

type StorySummary = {
  storyId: string;
  title: string;
  thumbnailDescription: string;
  minPlayers: number;
  maxPlayers: number;
};

type Props = {
  summary: StorySummary;
};

const StoryCard = ({ summary }: Props) => {
  const {
    title,
    thumbnailDescription: description,
    minPlayers,
    maxPlayers,
  } = summary;

  const fixedPlayerCount = minPlayers === maxPlayers;

  return (
    <Card className="py-0 overflow-clip h-full">
      <div className="flex h-full">
        <div className="w-1/3 flex-shrink-0 flex items-center justify-center bg-gray-100">
          <img
            src="https://placehold.co/100x100"
            alt={title}
            className="object-cover w-full h-full"
          />
        </div>
        <div className="w-2/3 p-4">
          <CardHeader className="p-0 pb-2">
            <div className="font-bold text-lg">{title}</div>
            <div className="text-sm text-muted-foreground">
              For{" "}
              {fixedPlayerCount ? minPlayers : `${minPlayers} - ${maxPlayers}`}{" "}
              players.
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
