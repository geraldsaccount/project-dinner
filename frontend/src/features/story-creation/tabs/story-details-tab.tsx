import SectionHeader from "@/components/shared/section-header";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import { StoryCreationDto } from "@/types";
import { useRef, useState } from "react";

type Props = {
  story: StoryCreationDto;
  setStory: (story: StoryCreationDto) => void;
};

const StoryDetailsTab = ({ story, setStory }: Props) => {
  const [bannerPreview, setBannerPreview] = useState<string>(
    story.bannerImage ||
      "https://placehold.co/600x300/e2e8f0/64748b?text=Banner+Preview"
  );
  const fileInputRef = useRef<HTMLInputElement | null>(null);

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      const previewUrl = URL.createObjectURL(file);
      setBannerPreview(previewUrl);
      setStory({ ...story, bannerImage: previewUrl });
    }
  };

  const handleUpdate = (field: keyof StoryCreationDto, value: string) =>
    setStory({ ...story, [field]: value });

  return (
    <div className="space-y-8">
      <SectionHeader title="Story Details" />
      <div className="grid gap-8 md:grid-cols-[2fr_1fr] items-start">
        <Card>
          <CardHeader>
            <CardTitle>Core Information</CardTitle>
          </CardHeader>
          <CardContent className="space-y-4">
            <div>
              <Label htmlFor="story-title">Story Title</Label>
              <Input
                id="story-title"
                value={story.title}
                onChange={(e) => handleUpdate("title", e.target.value)}
                placeholder="Example Title"
              />
            </div>
            <div>
              <Label htmlFor="shop-description">Shop Description</Label>
              <Textarea
                id="shop-description"
                value={story.shopDescription}
                onChange={(e) =>
                  handleUpdate("shopDescription", e.target.value)
                }
                placeholder="A short summary for potential buyers."
              />
            </div>
          </CardContent>
        </Card>
        <Card>
          <CardHeader>
            <CardTitle>Assets & Visuals</CardTitle>
          </CardHeader>
          <CardContent>
            <Label>Banner Image</Label>
            <div className="mt-2 items-center gap-4 space-y-2">
              <img
                className="w-full h-24 object-cover rounded-md bg-slate-100"
                src={bannerPreview}
                alt="Banner Preview"
              />
              <input
                type="file"
                accept="image/*"
                onChange={handleFileChange}
                className="hidden"
                ref={fileInputRef}
              />
              <Button
                type="button"
                onClick={() =>
                  fileInputRef.current && fileInputRef.current.click()
                }
              >
                Upload Banner
              </Button>
            </div>
          </CardContent>
        </Card>
      </div>
      <div className="grid md:grid-cols-2 gap-8">
        <Card>
          <CardHeader>
            <CardTitle>Game Rules</CardTitle>
          </CardHeader>
          <CardContent>
            <Textarea
              id="rules"
              value={story.rules}
              onChange={(e) => handleUpdate("rules", e.target.value)}
              rows={10}
              placeholder="1. One does not talk about the fight club"
            />
          </CardContent>
        </Card>
        <Card>
          <CardHeader>
            <CardTitle>Setting & Background</CardTitle>
          </CardHeader>
          <CardContent>
            <Textarea
              id="setting"
              value={story.setting}
              onChange={(e) => handleUpdate("setting", e.target.value)}
              rows={10}
            />
          </CardContent>
        </Card>
      </div>
    </div>
  );
};

export default StoryDetailsTab;
