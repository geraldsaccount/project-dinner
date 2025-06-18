import SectionHeader from "@/components/shared/section-header";
import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import { Stage } from "@/types/creation";
import { PlusCircle, Trash2 } from "lucide-react";
import { useEditorContext } from "../../context/editor-context";

const assignStageOrders = (stages: Stage[]): Stage[] =>
  stages.map((stage, idx) => ({ ...stage, order: idx }));

const StagesTab = () => {
  const { story, setStory, stages, setStages } = useEditorContext();

  const addStage = () => {
    const newStage: Stage = {
      id: `stage_${Date.now()}`,
      title: `Stage ${stages.length + 1}`,
      hostPrompt: "",
      order: stages.length,
    };
    setStages((prev) => assignStageOrders([...prev, newStage]));
  };

  const updateStage = (id: string, field: keyof Stage, value: string) => {
    setStages((prev) =>
      assignStageOrders(
        prev.map((s) => (s.id === id ? { ...s, [field]: value } : s))
      )
    );
  };

  const deleteStage = (id: string) => {
    if (
      window.confirm(
        "Are you sure? This will remove this stage from all characters as well."
      )
    ) {
      setStages((prev) => assignStageOrders(prev.filter((s) => s.id !== id)));
    }
  };

  return (
    <div className="space-y-4">
      <SectionHeader title="Stages & Prompts" />
      <Card>
        <CardHeader>
          <CardTitle>Pre-Game Briefing</CardTitle>
          <CardDescription>
            Sent to players before the game starts.
          </CardDescription>
        </CardHeader>
        <CardContent>
          <Textarea
            rows={8}
            value={story.briefing}
            onChange={(e) =>
              setStory((prev) => ({ ...prev, briefing: e.target.value }))
            }
            placeholder="Welcome to The Midnight Masquerade Murder..."
          />
        </CardContent>
      </Card>
      <Card>
        <CardHeader className="flex justify-between">
          <CardTitle>Game Stages</CardTitle>
          <Button variant="secondary" onClick={addStage}>
            <PlusCircle />
            Add Stage
          </Button>
        </CardHeader>
        <CardContent className="space-y-4">
          {stages.map((stage) => (
            <div key={stage.id} className="p-4 border rounded-lg space-y-2">
              <div className="flex justify-between items-center">
                <Input
                  value={stage.title}
                  onChange={(e) =>
                    updateStage(stage.id, "title", e.target.value)
                  }
                  className="font-semibold text-lg border-0 bg-transparent"
                />
                <Button
                  variant="destructive"
                  size="icon"
                  onClick={() => deleteStage(stage.id)}
                >
                  <Trash2 />
                </Button>
              </div>
              <Label>Host Prompt for end of "{stage.title}"</Label>
              <Textarea
                rows={4}
                value={stage.hostPrompt}
                onChange={(e) =>
                  updateStage(stage.id, "hostPrompt", e.target.value)
                }
                placeholder="The clock strikes ten..."
              />
            </div>
          ))}
          {stages.length === 0 && (
            <p className="text-center text-muted-foreground py-4">
              No stages defined yet.
            </p>
          )}
        </CardContent>
      </Card>
    </div>
  );
};

export default StagesTab;
