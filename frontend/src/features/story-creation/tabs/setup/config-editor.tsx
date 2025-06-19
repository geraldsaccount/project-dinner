import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Checkbox } from "@/components/ui/checkbox";
import { Label } from "@/components/ui/label";
import { cn } from "@/lib/utils";
import { Character, PlayerConfig } from "@/types/creation";
import { Trash2 } from "lucide-react";

type Props = {
  config: PlayerConfig;
  characters: Character[];
  countColor: string;
  selectedCount: number;
  deletePlayerConfig: (id: string) => void;
  toggleCharacterInConfig: (configId: string, charId: string) => void;
};

const ConfigEditor = ({
  config,
  characters,
  countColor,
  selectedCount,
  deletePlayerConfig,
  toggleCharacterInConfig,
}: Props) => {
  const getAvatarPreview = (char: Character) => {
    if (char.avatarImage instanceof File) {
      return URL.createObjectURL(char.avatarImage);
    }
    return char.avatarImage || "https://placehold.co/256x256/e2e8f0/64748b?text=Avatar+Preview";
  };

  return (
    <Card key={config.id}>
      <CardHeader className="flex justify-between">
        <div>
          <CardTitle>For {config.playerCount} Players</CardTitle>
          <CardDescription className={cn("font-semibold", countColor)}>
            {selectedCount} / {config.playerCount} characters selected
          </CardDescription>
        </div>
        <Button
          variant="destructive"
          size="icon"
          onClick={() => deletePlayerConfig(config.id)}
        >
          <Trash2 />
        </Button>
      </CardHeader>
      <CardContent>
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
          {characters.map((char: Character) => (
            <Label
              key={char.id}
              className={cn(
                "flex items-center gap-3 p-3 border rounded-lg transition-colors hover:bg-muted",
                config.characterIds.includes(char.id) &&
                  "bg-muted border-muted-foreground"
              )}
            >
              <Checkbox
                disabled={
                  config.characterIds.includes(char.id) && char.isPrimary
                }
                checked={config.characterIds.includes(char.id)}
                onCheckedChange={() =>
                  toggleCharacterInConfig(config.id, char.id)
                }
              />
              <img
                src={getAvatarPreview(char)}
                className="w-10 h-10 rounded-full object-cover"
                // Revoke the object URL when the image is no longer in view
                // This is a simple cleanup to prevent memory leaks in a list
                onLoad={(e) => {
                  const target = e.target as HTMLImageElement;
                  if (target.src.startsWith('blob:')) {
                    target.addEventListener('error', () => URL.revokeObjectURL(target.src));
                    target.addEventListener('abort', () => URL.revokeObjectURL(target.src));
                  }
                }}
              />
              <div className="flex flex-col">
                <span className="font-medium text-sm">{char.name}</span>
                {char.isPrimary ? (
                  <span className="font-medium text-xs text-muted-foreground">
                    Primary
                  </span>
                ) : null}
              </div>
            </Label>
          ))}
        </div>
      </CardContent>
    </Card>
  );
};

export default ConfigEditor;
